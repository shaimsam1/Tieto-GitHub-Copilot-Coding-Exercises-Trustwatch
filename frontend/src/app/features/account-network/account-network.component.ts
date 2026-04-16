import { Component, inject, OnInit, DestroyRef } from '@angular/core';
import { CommonModule, DatePipe, CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { forkJoin } from 'rxjs';
import { AccountService, TransactionService } from '@core/services/index';
import { Account, TransactionEdge, RiskRating } from '@models/index';
import { 
  AccountTypeBadgeComponent, 
  SeverityBadgeComponent, 
  LoadingSpinnerComponent 
} from '@shared/components/index';

interface NetworkNode {
  account: Account;
  incomingEdges: TransactionEdge[];
  outgoingEdges: TransactionEdge[];
  totalIncoming: number;
  totalOutgoing: number;
  flaggedConnections: number;
}

@Component({
  selector: 'app-account-network',
  standalone: true,
  imports: [
    CommonModule,
    DatePipe,
    CurrencyPipe,
    FormsModule,
    RouterLink,
    AccountTypeBadgeComponent,
    SeverityBadgeComponent,
    LoadingSpinnerComponent
  ],
  templateUrl: './account-network.component.html',
  styleUrl: './account-network.component.scss'
})
export class AccountNetworkComponent implements OnInit {
  private accountService = inject(AccountService);
  private transactionService = inject(TransactionService);
  private destroyRef = inject(DestroyRef);

  accounts: Account[] = [];
  edges: TransactionEdge[] = [];
  networkNodes: NetworkNode[] = [];
  filteredNodes: NetworkNode[] = [];
  
  selectedAccount: Account | null = null;
  selectedAccountEdges: TransactionEdge[] = [];
  
  isLoading = true;
  error: string | null = null;
  
  // Filter state
  showFlaggedOnly = false;
  selectedRiskRating: RiskRating | 'ALL' = 'ALL';
  searchTerm = '';
  
  riskRatings: (RiskRating | 'ALL')[] = ['ALL', 'HIGH', 'MEDIUM', 'LOW'];

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.isLoading = true;
    this.error = null;

    forkJoin({
      accounts: this.accountService.getAccounts(),
      edges: this.transactionService.getTransactionEdges()
    })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: ({ accounts, edges }) => {
          this.accounts = accounts;
          this.edges = edges;
          this.buildNetworkNodes();
          this.applyFilters();
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Failed to load network data:', err);
          this.error = 'Failed to load account network. Please try again.';
          this.isLoading = false;
        }
      });
  }

  private buildNetworkNodes(): void {
    this.networkNodes = this.accounts.map(account => {
      const incomingEdges = this.edges.filter(e => e.toAccountId === account.accountId);
      const outgoingEdges = this.edges.filter(e => e.fromAccountId === account.accountId);
      
      return {
        account,
        incomingEdges,
        outgoingEdges,
        totalIncoming: incomingEdges.reduce((sum, e) => sum + e.amount, 0),
        totalOutgoing: outgoingEdges.reduce((sum, e) => sum + e.amount, 0),
        flaggedConnections: [...incomingEdges, ...outgoingEdges].filter(e => e.isFlagged).length
      };
    });
  }

  applyFilters(): void {
    let result = [...this.networkNodes];

    if (this.showFlaggedOnly) {
      result = result.filter(n => n.flaggedConnections > 0 || n.account.amlFlags.length > 0);
    }

    if (this.selectedRiskRating !== 'ALL') {
      result = result.filter(n => n.account.riskRating === this.selectedRiskRating);
    }

    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase();
      result = result.filter(n => 
        n.account.customerName.toLowerCase().includes(term) ||
        n.account.accountId.toLowerCase().includes(term) ||
        n.account.iban.toLowerCase().includes(term)
      );
    }

    // Sort by flagged connections desc, then by risk
    const riskOrder: Record<RiskRating, number> = { HIGH: 0, MEDIUM: 1, LOW: 2 };
    result.sort((a, b) => {
      const flagDiff = b.flaggedConnections - a.flaggedConnections;
      if (flagDiff !== 0) return flagDiff;
      return riskOrder[a.account.riskRating] - riskOrder[b.account.riskRating];
    });

    this.filteredNodes = result;
  }

  onFilterChange(): void {
    this.applyFilters();
  }

  selectAccount(node: NetworkNode): void {
    if (this.selectedAccount?.accountId === node.account.accountId) {
      this.selectedAccount = null;
      this.selectedAccountEdges = [];
    } else {
      this.selectedAccount = node.account;
      this.selectedAccountEdges = [...node.incomingEdges, ...node.outgoingEdges]
        .sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime());
    }
  }

  getAccountName(accountId: string): string {
    const account = this.accounts.find(a => a.accountId === accountId);
    return account?.customerName || accountId;
  }

  getEdgeDirection(edge: TransactionEdge): 'incoming' | 'outgoing' {
    return edge.toAccountId === this.selectedAccount?.accountId ? 'incoming' : 'outgoing';
  }

  clearFilters(): void {
    this.showFlaggedOnly = false;
    this.selectedRiskRating = 'ALL';
    this.searchTerm = '';
    this.applyFilters();
  }

  clearSelection(): void {
    this.selectedAccount = null;
    this.selectedAccountEdges = [];
  }

  refresh(): void {
    this.clearSelection();
    this.loadData();
  }

  getRiskClass(rating: RiskRating): string {
    return `risk-${rating.toLowerCase()}`;
  }

  getFlaggedEdgesCount(): number {
    return this.edges.filter(e => e.isFlagged).length;
  }

  getIncomingEdgesCount(): number {
    return this.selectedAccountEdges.filter(e => this.getEdgeDirection(e) === 'incoming').length;
  }

  getOutgoingEdgesCount(): number {
    return this.selectedAccountEdges.filter(e => this.getEdgeDirection(e) === 'outgoing').length;
  }

  formatTypology(typology: string): string {
    return typology.replace(/_/g, ' ');
  }
}
