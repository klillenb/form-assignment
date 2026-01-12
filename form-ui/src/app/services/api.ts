import { inject, Injectable } from '@angular/core';
import { environment } from '../environment/environment';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { catchError, map, Observable, throwError } from 'rxjs';
import { Sector } from '../models/sector';
import { Form } from '../models/form';

@Injectable({
  providedIn: 'root',
})
export class Api {
  private readonly baseUrl: string = `${environment.apiBaseUrl}/forms`;

  private readonly http: HttpClient = inject(HttpClient);

  getSectors(): Observable<Sector[]> {
    return this.http.get<Sector[]>(`${this.baseUrl}/sectors`).pipe(catchError(this.handleError));
  }

  getForm(): Observable<Form | null> {
    return this.http.get<Form>(this.baseUrl).pipe(
      map((response: Form) => response ?? null),
      catchError(this.handleError)
    );
  }

  saveForm(form: Form): Observable<HttpResponse<Form>> {
    return this.http
      .post<Form>(this.baseUrl, form, { observe: 'response' })
      .pipe(catchError(this.handleError));
  }

  updateForm(form: Form, id: string): Observable<Form> {
    return this.http.put<Form>(`${this.baseUrl}/${id}`, form).pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    console.error(`API error:`, error);
    return throwError(() => new Error('Error communicating with backend!'));
  }
}
