import { TestBed } from '@angular/core/testing';

import { Api } from './api';
import {
  HttpClientTestingModule,
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { environment } from '../environment/environment';
import { Sector } from '../models/sector';
import { Form } from '../models/form';

describe('Api', () => {
  let service: Api;
  let httpMock: HttpTestingController;

  const baseUrl = `${environment.apiBaseUrl}/forms`;
  const mockSectors: Sector[] = [{ id: 1, name: 'A' }];
  const mockForm: Form = { id: 42, name: 'Test', hasAgreed: true, sectors: [1] };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [Api, provideHttpClientTesting()],
    });

    service = TestBed.inject(Api);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getSectors', () => {
    it('should return an array of sectors', () => {
      service.getSectors().subscribe((data) => {
        expect(data).toEqual(mockSectors);
      });

      const req = httpMock.expectOne(`${baseUrl}/sectors`);
      expect(req.request.method).toBe('GET');
      req.flush(mockSectors);
    });

    it('should handle error', () => {
      const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});
      service.getSectors().subscribe({
        next: () => {
          throw new Error('expected an error');
        },
        error: (err) => expect(err.message).toBe('Error communicating with backend!'),
      });

      const req = httpMock.expectOne(`${baseUrl}/sectors`);
      req.error(new ErrorEvent('Network error'));
      consoleSpy.mockRestore();
    });
  });

  describe('getForm', () => {
    it('should return a form', () => {
      service.getForm().subscribe((data) => {
        expect(data).toEqual(mockForm);
      });

      const req = httpMock.expectOne(baseUrl);
      expect(req.request.method).toBe('GET');
      req.flush(mockForm);
    });

    it('should return null if response is null', () => {
      service.getForm().subscribe((data) => {
        expect(data).toBeNull();
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush(null);
    });

    it('should handle error', () => {
      const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

      service.getForm().subscribe({
        next: () => {
          throw new Error('expected an error');
        },
        error: (err) => expect(err.message).toBe('Error communicating with backend!'),
      });

      const req = httpMock.expectOne(baseUrl);
      req.error(new ErrorEvent('Server error'));
      consoleSpy.mockRestore();
    });
  });

  describe('saveForm', () => {
    it('should return HttpResponse with Location header', () => {
      service.saveForm(mockForm).subscribe((resp) => {
        expect(resp.body).toEqual(mockForm);
        expect(resp.headers.get('Location')).toBe('/forms/42');
        expect(resp.status).toBe(201);
      });

      const req = httpMock.expectOne(baseUrl);
      expect(req.request.method).toBe('POST');
      req.flush(mockForm, { status: 201, statusText: 'Created', headers: { Location: '/forms/42' } });
    });

    it('should handle error', () => {
      const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

      service.saveForm(mockForm).subscribe({
        next: () => {
          throw new Error('expected an error');
        },
        error: (err) => expect(err.message).toBe('Error communicating with backend!'),
      });

      const req = httpMock.expectOne(baseUrl);
      req.error(new ErrorEvent('Server error'));
      consoleSpy.mockRestore();
    });
  });

  describe('updateForm', () => {
    it('should return updated form', () => {
      service.updateForm(mockForm, '42').subscribe((resp) => {
        expect(resp).toEqual(mockForm);
      });

      const req = httpMock.expectOne(`${baseUrl}/42`);
      expect(req.request.method).toBe('PUT');
      req.flush(mockForm);
    });

    it('should handle error', () => {
      const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

      service.updateForm(mockForm, '42').subscribe({
        next: () => {
          throw new Error('expected an error');
        },
        error: (err) => expect(err.message).toBe('Error communicating with backend!'),
      });

      const req = httpMock.expectOne(`${baseUrl}/42`);
      req.error(new ErrorEvent('Server error'));
      consoleSpy.mockRestore();
    });
  });
});
