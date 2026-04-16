import { Component, inject, OnInit, DestroyRef } from '@angular/core';
import { CommonModule, AsyncPipe, CurrencyPipe, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { forkJoin } from 'rxjs';
import { AccountService, TransactionService, AmlFlagService } from '@core/services/index';
import { Account, Transaction, AmlFlag } from '@models/index';
import { SeverityBadgeComponent, StatusBadgeComponent, RiskIndicatorComponent, LoadingSpinnerComponent } from '@shared/components/index';

interface DashboardStats {
  totalAccounts: number;
  highRiskAccounts: number;
  flaggedAccounts: number;
  criticalFlags: number;
  pendingTransactions: number;
  blockedTransactions: number;
}

@Component({
  selector: 'app-aml-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    AsyncPipe,
    CurrencyPipe,
    DatePipe,
    RouterLink,
    SeverityBadgeComponent,
    StatusBadgeComponent,
    RiskIndicatorComponent,
    LoadingSpinnerComponent
  ],
  templateUrl: './aml-dashboard.component.html',
  styleUrl: './aml-dashboard.component.scss'
})
export class AmlDashboardComponent implements OnInit {
  private accountService = inject(AccountService);
  private transactionService = inject(TransactionService);
  private amlFlagService = inject(AmlFlagService);
  private destroyRef = inject(DestroyRef);

  accounts: Account[] = [];
  transactions: Transaction[] = [];
  flags: AmlFlag[] = [];
  stats: DashboardStats | null = null;
  isLoading = true;
  error: string | null = null;

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.isLoading = true;
    this.error = null;

    forkJoin({
      accounts: this.accountService.getAccounts(),
      transactions: this.transactionService.getTransactions(),
      flags: this.amlFlagService.getFlags()
    })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: ({ accounts, transactions, flags }) => {
          this.accounts = accounts;
          this.transactions = transactions;
          this.flags = flags;
          this.calculateStats();
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Dashboard load error:', err);
          this.error = 'Failed to load dashboard data. Please try again.';
          this.isLoading = false;
        }
      });
  }

  private calculateStats(): void {
    this.stats = {
      totalAccounts: this.accounts.length,
      highRiskAccounts: this.accounts.filter(a => a.riskRating === 'HIGH').length,
      flaggedAccounts: this.accounts.filter(a => a.amlFlags.length > 0).length,
      criticalFlags: this.flags.filter(f => f.severity === 'CRITICAL').length,
      pendingTransactions: this.transactions.filter(t => t.status === 'PENDING_REVIEW').length,
      blockedTransactions: this.transactions.filter(t => t.status === 'BLOCKED').length
    };
  }

  getRecentFlags(): AmlFlag[] {
    return [...this.flags]
      .sort((a, b) => new Date(b.detectedAt).getTime() - new Date(a.detectedAt).getTime())
      .slice(0, 5);
  }

  getHighRiskTransactions(): Transaction[] {
    return [...this.transactions]
      .filter(t => t.riskScore >= 70)
      .sort((a, b) => b.riskScore - a.riskScore)
      .slice(0, 5);
  }

  refresh(): void {
    this.loadDashboardData();
  }
}
