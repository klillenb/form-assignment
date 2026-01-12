import { Component, inject, OnInit } from '@angular/core';
import { Api } from '../../services/api';
import { Observable } from 'rxjs';
import { Sector } from '../../models/sector';
import { Dropdown } from '../dropdown/dropdown';
import { AsyncPipe } from '@angular/common';
import { MatLabel } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Form } from '../../models/form';
import { Toast } from '../../services/toast';
import { Messages } from '../../constants/messages';

@Component({
  selector: 'app-input-form',
  imports: [
    Dropdown,
    AsyncPipe,
    MatLabel,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatButtonModule,
    ReactiveFormsModule,
  ],
  templateUrl: './input-form.html',
  styleUrl: './input-form.css',
})
export class InputForm implements OnInit {
  private readonly api: Api = inject(Api);
  private readonly fb: FormBuilder = inject(FormBuilder);
  private readonly toast: Toast = inject(Toast);

  formId?: string;

  sectors$: Observable<Sector[]> = this.api.getSectors();

  form = this.fb.nonNullable.group({
    name: ['', Validators.required],
    sectors: [[] as number[], Validators.required],
    hasAgreed: [false, Validators.requiredTrue],
  });

  ngOnInit(): void {
    this.loadForm();
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const formValue = this.form.getRawValue();

    if (!this.formId) {
      this.api.saveForm(formValue).subscribe({
        next: (response) => {
          const location = response.headers.get('Location');

          if (!location) {
            this.toast.error(Messages.INVALID_SERVER_RESPONSE);
            return;
          }

          this.formId = location.split('/').pop();

          this.toast.success(Messages.FORM_SAVED);
        },
        error: () => {
          this.toast.error(Messages.FORM_SAVE_FAILED);
        },
      });
    } else {
      this.api.updateForm(formValue, this.formId).subscribe({
        next: () => {
          this.toast.success(Messages.FORM_UPDATED);
        },
        error: () => {
          this.toast.error(Messages.FORM_UPDATE_FAILED);
        },
      });
    }
  }

  private loadForm(): void {
    this.api.getForm().subscribe({
      next: (data) => {
        if (!data) {
          return;
        }

        this.fillForm(data);
      },
      error: () => {
        this.toast.error(Messages.FORM_GET_FAILED);
      },
    });
  }

  private fillForm(data: Form): void {
    this.formId = data.id?.toString();

    this.form.patchValue({
      name: data.name,
      hasAgreed: data.hasAgreed,
      sectors: data.sectors,
    });
  }
}
