import { inject, Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class Toast {
  private readonly snackbar: MatSnackBar = inject(MatSnackBar);

  success(message: string, duration: number = 3000): void {
    this.snackbar.open(message, 'OK', {
      duration,
      panelClass: ['snackbar-success'],
    });
  }

  error(message: string, duration: number = 5000): void {
    this.snackbar.open(message, 'Dismiss', {
      duration,
      panelClass: ['snackbar-error'],
    });
  }
}
