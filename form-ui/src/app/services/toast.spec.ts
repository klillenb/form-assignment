import { TestBed } from '@angular/core/testing';

import { Toast } from './toast';
import { MatSnackBar } from '@angular/material/snack-bar';

describe('Toast', () => {
  let service: Toast;
  let matSnackBarMock: Partial<MatSnackBar>;

  beforeEach(() => {
    matSnackBarMock = {
      open: vi.fn(),
    };

    TestBed.configureTestingModule({
      providers: [Toast, { provide: MatSnackBar, useValue: matSnackBarMock }],
    });
    service = TestBed.inject(Toast);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('success', () => {
    it('should call snackbar.open with correct arguments', () => {
      service.success('Success message');

      expect(matSnackBarMock.open).toHaveBeenCalledWith('Success message', 'OK', {
        duration: 3000,
        panelClass: ['snackbar-success'],
      });
    });

    it('should allow custom duration', () => {
      service.success('Success message', 1000);

      expect(matSnackBarMock.open).toHaveBeenCalledWith('Success message', 'OK', {
        duration: 1000,
        panelClass: ['snackbar-success'],
      });
    });
  });

  describe('error', () => {
    it('should call snackbar.open with correct arguments', () => {
      service.error('Error message');

      expect(matSnackBarMock.open).toHaveBeenCalledWith('Error message', 'Dismiss', {
        duration: 5000,
        panelClass: ['snackbar-error'],
      });
    });

    it('should allow custom duration', () => {
      service.error('Error message', 2000);

      expect(matSnackBarMock.open).toHaveBeenCalledWith('Error message', 'Dismiss', {
        duration: 2000,
        panelClass: ['snackbar-error'],
      });
    });
  });
});
