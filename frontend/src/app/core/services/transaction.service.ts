import { Injectable, inject } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ApiService } from './api.service';
import { Transaction, TransactionEdge } from '@models/index';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private apiService = inject(ApiService);
  
  private transactionsSubject = new BehaviorSubject<Transaction[]>([]);
  private edgesSubject = new BehaviorSubject<TransactionEdge[]>([]);

  transactions$ = this.transactionsSubject.asObservable();
  edges$ = this.edgesSubject.asObservable();

  getTransactions(): Observable<Transaction[]> {
    return this.apiService.get<Transaction[]>('/api/transactions').pipe(
      tap(transactions => this.transactionsSubject.next(transactions))
    );
  }

  getTransactionById(transactionId: string): Observable<Transaction> {
    return this.apiService.get<Transaction>(`/api/transactions/${transactionId}`);
  }

  getTransactionsByAccount(accountId: string): Observable<Transaction[]> {
    return this.apiService.get<Transaction[]>(`/api/transactions?accountId=${accountId}`);
  }

  getHighRiskTransactions(minScore: number = 70): Observable<Transaction[]> {
    return this.apiService.get<Transaction[]>(`/api/transactions/high-risk?minScore=${minScore}`);
  }

  getPendingReviewTransactions(): Observable<Transaction[]> {
    return this.apiService.get<Transaction[]>('/api/transactions?status=PENDING_REVIEW');
  }

  getBlockedTransactions(): Observable<Transaction[]> {
    return this.apiService.get<Transaction[]>('/api/transactions?status=BLOCKED');
  }

  getTransactionEdges(): Observable<TransactionEdge[]> {
    return this.apiService.get<TransactionEdge[]>('/api/transaction-edges').pipe(
      tap(edges => this.edgesSubject.next(edges))
    );
  }

  getFlaggedEdges(): Observable<TransactionEdge[]> {
    return this.apiService.get<TransactionEdge[]>('/api/transaction-edges?flagged=true');
  }
}
