import { inject, Injectable } from '@angular/core';
import { environment } from '../environment/environment';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { Sector } from '../models/sector';
import { Form } from '@angular/forms';

@Injectable({
  providedIn: 'root',
})
export class Api {
  private readonly baseUrl: string = `${environment.apiBaseUrl}/forms`;

  private readonly http: HttpClient = inject(HttpClient);

  getSectors(): Observable<Sector[]> {
    return this.http.get<Sector[]>(`${this.baseUrl}/sectors`).pipe(catchError(this.handleError));
  }

  saveForm(form: Form): void {
    this.http.post<Form>(this.baseUrl, form).pipe(catchError(this.handleError));
  }

  updateForm(form: Form): void {
    this.http.patch<Form>(this.baseUrl, form).pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    console.error(`API error:`, error);
    return throwError(() => new Error('Error communicating with backend!'));
  }
}
