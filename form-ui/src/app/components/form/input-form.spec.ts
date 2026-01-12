import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InputForm } from './input-form';
import { Api } from '../../services/api';
import { Toast } from '../../services/toast';
import { of, throwError } from 'rxjs';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { Dropdown } from '../dropdown/dropdown';
import { Form } from '../../models/form';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Messages } from '../../constants/messages';

describe('InputForm', () => {
  let component: InputForm;
  let fixture: ComponentFixture<InputForm>;

  let apiMock: Partial<Api>;
  let toastMock: Partial<Toast>;

  const mockSectors = [{ id: 1, name: 'A' }];
  const mockFormData = { id: 42, name: 'Test', hasAgreed: true, sectors: [1] };

  beforeEach(async () => {
    apiMock = {
      getSectors: vi.fn(() => of(mockSectors)),
      getForm: vi.fn(() => of(mockFormData)),
      saveForm: vi.fn((form: Form) =>
        of(
          new HttpResponse({
            body: form,
            headers: new HttpHeaders({ Location: '/forms/42' }),
            status: 201,
          })
        )
      ),
      updateForm: vi.fn((form: Form) => of(form)),
    };

    toastMock = {
      success: vi.fn(),
      error: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatCheckboxModule,
        MatButtonModule,
      ],
      providers: [
        FormBuilder,
        { provide: Api, useValue: apiMock },
        { provide: Toast, useValue: toastMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(InputForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load form data on init', () => {
    component.ngOnInit();
    expect(apiMock.getForm).toHaveBeenCalled();
    expect(component.form.value).toEqual({
      name: 'Test',
      sectors: [1],
      hasAgreed: true,
    });
    expect(component.formId).toBe('42');
  });

  describe('submit()', () => {
    it('should mark all as touched if form is invalid', () => {
      component.form.patchValue({ name: '', sectors: [], hasAgreed: false });
      const markAllTouchedSpy = vi.spyOn(component.form, 'markAllAsTouched');

      component.submit();

      expect(markAllTouchedSpy).toHaveBeenCalled();
      expect(apiMock.saveForm).not.toHaveBeenCalled();
      expect(apiMock.updateForm).not.toHaveBeenCalled();
    });

    it('should save form if formId is not set', () => {
      component.formId = undefined;
      component.form.patchValue({ name: 'Test', sectors: [1], hasAgreed: true });

      component.submit();

      expect(apiMock.saveForm).toHaveBeenCalledWith(component.form.getRawValue());
      expect(component.formId).toBe('42');
      expect(toastMock.success).toHaveBeenCalledWith(Messages.FORM_SAVED);
    });

    it('should handle saveForm error', () => {
      apiMock.saveForm = vi.fn(() => throwError(() => new Error('Fail')));
      component.formId = undefined;
      component.form.patchValue({ name: 'Test', sectors: [1], hasAgreed: true });

      component.submit();

      expect(toastMock.error).toHaveBeenCalledWith(Messages.FORM_SAVE_FAILED);
    });

    it('should update form if formId is set', () => {
      component.formId = '42';
      component.form.patchValue({ name: 'Test', sectors: [1], hasAgreed: true });

      component.submit();

      expect(apiMock.updateForm).toHaveBeenCalledWith(component.form.getRawValue(), '42');
      expect(toastMock.success).toHaveBeenCalledWith(Messages.FORM_UPDATED);
    });

    it('should handle updateForm error', () => {
      apiMock.updateForm = vi.fn(() => throwError(() => new Error('Fail')));
      component.formId = '42';
      component.form.patchValue({ name: 'Test', sectors: [1], hasAgreed: true });

      component.submit();

      expect(toastMock.error).toHaveBeenCalledWith(Messages.FORM_UPDATE_FAILED);
    });
  });
});
