import { Injectable, inject } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ApiService } from './api.service';
import { Account } from '@models/index';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private apiService = inject(ApiService);
  
  private accountsSubject = new BehaviorSubject<Account[]>([]);
  private selectedAccountSubject = new BehaviorSubject<Account | null>(null);

  accounts$ = this.accountsSubject.asObservable();
  selectedAccount$ = this.selectedAccountSubject.asObservable();

  getAccounts(): Observable<Account[]> {
    return this.apiService.get<Account[]>('/api/accounts').pipe(
      tap(accounts => this.accountsSubject.next(accounts))
    );
  }

  getAccountById(accountId: string): Observable<Account> {
    return this.apiService.get<Account>(`/api/accounts/${accountId}`).pipe(
      tap(account => this.selectedAccountSubject.next(account))
    );
  }

  getHighRiskAccounts(): Observable<Account[]> {
    return this.apiService.get<Account[]>('/api/accounts?riskRating=HIGH');
  }

  getFlaggedAccounts(): Observable<Account[]> {
    return this.apiService.get<Account[]>('/api/accounts/flagged');
  }

  selectAccount(account: Account | null): void {
    this.selectedAccountSubject.next(account);
  }

  clearSelection(): void {
    this.selectedAccountSubject.next(null);
  }
}
