import { Component, inject, OnInit, DestroyRef } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { AmlFlagService, AccountService } from '@core/services/index';
import { AmlFlag, Account, Severity, AmlTypology } from '@models/index';
import { SeverityBadgeComponent, LoadingSpinnerComponent } from '@shared/components/index';

@Component({
  selector: 'app-pattern-viewer',
  standalone: true,
  imports: [
    CommonModule,
    DatePipe,
    FormsModule,
    RouterLink,
    SeverityBadgeComponent,
    LoadingSpinnerComponent
  ],
  templateUrl: './pattern-viewer.component.html',
  styleUrl: './pattern-viewer.component.scss'
})
export class PatternViewerComponent implements OnInit {
  private amlFlagService = inject(AmlFlagService);
  private accountService = inject(AccountService);
  private destroyRef = inject(DestroyRef);

  flags: AmlFlag[] = [];
  accounts: Account[] = [];
  filteredFlags: AmlFlag[] = [];
  
  isLoading = true;
  error: string | null = null;
  
  // Filter state
  selectedSeverity: Severity | 'ALL' = 'ALL';
  selectedTypology: AmlTypology | 'ALL' = 'ALL';
  searchTerm = '';
  
  severities: (Severity | 'ALL')[] = ['ALL', 'CRITICAL', 'HIGH', 'MEDIUM', 'LOW'];
  typologies: (AmlTypology | 'ALL')[] = [
    'ALL',
    'STRUCTURING',
    'LAYERING',
    'ROUND_TRIPPING',
    'SHELL_NETWORK',
    'VELOCITY_ANOMALY',
    'SMURFING'
  ];

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.isLoading = true;
    this.error = null;

    this.amlFlagService.getFlags()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (flags) => {
          this.flags = flags;
          this.applyFilters();
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Failed to load flags:', err);
          this.error = 'Failed to load AML patterns. Please try again.';
          this.isLoading = false;
        }
      });

    this.accountService.getAccounts()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (accounts) => {
          this.accounts = accounts;
        }
      });
  }

  applyFilters(): void {
    let result = [...this.flags];

    if (this.selectedSeverity !== 'ALL') {
      result = result.filter(f => f.severity === this.selectedSeverity);
    }

    if (this.selectedTypology !== 'ALL') {
      result = result.filter(f => f.typology === this.selectedTypology);
    }

    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase();
      result = result.filter(f => 
        f.flagId.toLowerCase().includes(term) ||
        f.typology.toLowerCase().includes(term) ||
        f.recommendedAction.toLowerCase().includes(term) ||
        (f.accountId && f.accountId.toLowerCase().includes(term))
      );
    }

    // Sort by severity priority and then by date
    const severityOrder: Record<Severity, number> = { CRITICAL: 0, HIGH: 1, MEDIUM: 2, LOW: 3 };
    result.sort((a, b) => {
      const severityDiff = severityOrder[a.severity] - severityOrder[b.severity];
      if (severityDiff !== 0) return severityDiff;
      return new Date(b.detectedAt).getTime() - new Date(a.detectedAt).getTime();
    });

    this.filteredFlags = result;
  }

  onFilterChange(): void {
    this.applyFilters();
  }

  getAccountName(accountId: string | undefined): string {
    if (!accountId) return 'Unknown';
    const account = this.accounts.find(a => a.accountId === accountId);
    return account?.customerName || accountId;
  }

  getSeverityCount(severity: Severity): number {
    return this.flags.filter(f => f.severity === severity).length;
  }

  getTypologyCount(typology: AmlTypology): number {
    return this.flags.filter(f => f.typology === typology).length;
  }

  clearFilters(): void {
    this.selectedSeverity = 'ALL';
    this.selectedTypology = 'ALL';
    this.searchTerm = '';
    this.applyFilters();
  }

  refresh(): void {
    this.loadData();
  }

  formatText(text: string): string {
    return text.replace(/_/g, ' ');
  }
}
