import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '@environments/environment';
import { ApiResponse, ApiError } from '@models/index';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private http = inject(HttpClient);
  private baseUrl = environment.apiUrl;

  get<T>(endpoint: string): Observable<T> {
    return this.http.get<ApiResponse<T>>(`${this.baseUrl}${endpoint}`).pipe(
      map(response => this.extractData(response)),
      catchError(error => this.handleError(error))
    );
  }

  post<T, R>(endpoint: string, body: T): Observable<R> {
    return this.http.post<ApiResponse<R>>(`${this.baseUrl}${endpoint}`, body).pipe(
      map(response => this.extractData(response)),
      catchError(error => this.handleError(error))
    );
  }

  put<T, R>(endpoint: string, body: T): Observable<R> {
    return this.http.put<ApiResponse<R>>(`${this.baseUrl}${endpoint}`, body).pipe(
      map(response => this.extractData(response)),
      catchError(error => this.handleError(error))
    );
  }

  delete<T>(endpoint: string): Observable<T> {
    return this.http.delete<ApiResponse<T>>(`${this.baseUrl}${endpoint}`).pipe(
      map(response => this.extractData(response)),
      catchError(error => this.handleError(error))
    );
  }

  private extractData<T>(response: ApiResponse<T>): T {
    if (response.errors && response.errors.length > 0) {
      throw new ApiServiceError(response.errors);
    }
    return response.data;
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let apiErrors: ApiError[];

    if (error.error && error.error.errors) {
      apiErrors = error.error.errors;
    } else {
      apiErrors = [{
        code: 'HTTP_ERROR',
        message: error.message || 'An unexpected error occurred'
      }];
    }

    console.error('API Error:', apiErrors);
    return throwError(() => new ApiServiceError(apiErrors));
  }
}

export class ApiServiceError extends Error {
  constructor(public errors: ApiError[]) {
    super(errors.map(e => e.message).join(', '));
    this.name = 'ApiServiceError';
  }
}
