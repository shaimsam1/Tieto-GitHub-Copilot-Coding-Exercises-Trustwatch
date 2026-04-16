import { Injectable, inject } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ApiService } from './api.service';
import { AmlFlag, Severity } from '@models/index';

@Injectable({
  providedIn: 'root'
})
export class AmlFlagService {
  private apiService = inject(ApiService);
  
  private flagsSubject = new BehaviorSubject<AmlFlag[]>([]);

  flags$ = this.flagsSubject.asObservable();

  getFlags(): Observable<AmlFlag[]> {
    return this.apiService.get<AmlFlag[]>('/api/alerts').pipe(
      tap(flags => this.flagsSubject.next(flags))
    );
  }

  getFlagById(flagId: string): Observable<AmlFlag> {
    return this.apiService.get<AmlFlag>(`/api/alerts/${flagId}`);
  }

  getFlagsByAccount(accountId: string): Observable<AmlFlag[]> {
    return this.apiService.get<AmlFlag[]>(`/api/alerts/account/${accountId}`);
  }

  getFlagsBySeverity(severity: Severity): Observable<AmlFlag[]> {
    return this.apiService.get<AmlFlag[]>(`/api/alerts/severity/${severity}`);
  }

  getCriticalFlags(): Observable<AmlFlag[]> {
    return this.apiService.get<AmlFlag[]>('/api/alerts/critical');
  }

  getHighPriorityFlags(): Observable<AmlFlag[]> {
    return this.apiService.get<AmlFlag[]>('/api/alerts/high');
  }
}
