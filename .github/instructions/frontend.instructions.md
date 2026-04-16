---
applyTo: "frontend/**,src/app/**,*.component.ts,*.service.ts,*.module.ts"
model: claude-sonnet-4-20250514
---

# Frontend Instructions — Angular 17+

## Project Structure

```
frontend/
├── src/
│   ├── app/
│   │   ├── core/                    # Singleton services, guards, interceptors
│   │   │   ├── services/
│   │   │   │   ├── api.service.ts
│   │   │   │   └── error-handler.service.ts
│   │   │   ├── interceptors/
│   │   │   └── guards/
│   │   ├── shared/                  # Reusable components, pipes, directives
│   │   │   ├── components/
│   │   │   ├── pipes/
│   │   │   └── directives/
│   │   ├── features/                # Feature modules
│   │   │   ├── aml-dashboard/
│   │   │   │   ├── aml-dashboard.component.ts
│   │   │   │   ├── aml-dashboard.component.html
│   │   │   │   ├── aml-dashboard.component.scss
│   │   │   │   └── services/
│   │   │   ├── pattern-viewer/
│   │   │   │   ├── pattern-viewer.component.ts
│   │   │   │   └── components/
│   │   │   │       ├── pattern-list/
│   │   │   │       └── pattern-detail/
│   │   │   └── account-network/
│   │   │       ├── account-network.component.ts
│   │   │       └── components/
│   │   │           ├── account-card/
│   │   │           └── network-table/
│   │   ├── models/                  # TypeScript interfaces
│   │   └── app.routes.ts
│   ├── environments/
│   └── styles/
├── angular.json
└── package.json
```

## Always

- Use **standalone components** (Angular 17+ default)
- Implement **smart/container** components for data fetching and state
- Implement **dumb/presentational** components for UI rendering
- Use **OnPush** change detection strategy for presentational components
- Define **TypeScript interfaces** for all API responses and models
- Use **RxJS operators** properly (unsubscribe via `takeUntilDestroyed()` or `async` pipe)
- Apply **kebab-case** for component selectors: `app-account-card`
- Apply **camelCase** for service names: `accountService`
- Use **environment files** for API base URLs

## Never

- **NEVER use graph or chart libraries** (D3, Chart.js, ngx-charts, etc.)
- **NEVER use CSS frameworks** unless explicitly approved
- **NEVER import entire RxJS** — import specific operators
- **NEVER use `any` type** — define proper interfaces
- **NEVER subscribe in templates** without async pipe for simple cases
- **NEVER put business logic in components** — use services

## Component Conventions

### Standalone Component Template

```typescript
import { Component, ChangeDetectionStrategy, inject, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-account-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './account-card.component.html',
  styleUrl: './account-card.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AccountCardComponent {
  // Use signal-based inputs (Angular 17+)
  account = input.required<Account>();
  
  // Use signal-based outputs
  selected = output<string>();
  
  onSelect(): void {
    this.selected.emit(this.account().accountId);
  }
}
```

### Smart Component Template

```typescript
import { Component, inject, OnInit } from '@angular/core';
import { CommonModule, AsyncPipe } from '@angular/common';
import { AccountService } from '../../core/services/account.service';
import { AccountCardComponent } from './components/account-card/account-card.component';

@Component({
  selector: 'app-account-network',
  standalone: true,
  imports: [CommonModule, AsyncPipe, AccountCardComponent],
  templateUrl: './account-network.component.html',
  styleUrl: './account-network.component.scss'
})
export class AccountNetworkComponent implements OnInit {
  private accountService = inject(AccountService);
  
  accounts$ = this.accountService.getAccounts();
  
  ngOnInit(): void {
    // Initialize data loading if needed
  }
  
  onAccountSelected(accountId: string): void {
    this.accountService.selectAccount(accountId);
  }
}
```

## Service Conventions

### API Service Pattern

```typescript
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, catchError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse, Account } from '../../models';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/api`;

  getAccounts(): Observable<Account[]> {
    return this.http.get<ApiResponse<Account[]>>(`${this.baseUrl}/accounts`).pipe(
      map(response => response.data),
      catchError(this.handleError)
    );
  }

  getAccountById(id: string): Observable<Account> {
    return this.http.get<ApiResponse<Account>>(`${this.baseUrl}/accounts/${id}`).pipe(
      map(response => response.data)
    );
  }

  private handleError(error: unknown): Observable<never> {
    console.error('API Error:', error);
    throw error;
  }
}
```

## TypeScript Interfaces

### API Response Models

```typescript
// models/api-response.model.ts
export interface ApiResponse<T> {
  data: T;
  meta: ApiMeta;
  errors: ApiError[];
}

export interface ApiMeta {
  timestamp: string;
  requestId: string;
}

export interface ApiError {
  code: string;
  message: string;
  field?: string;
}
```

### Domain Models

```typescript
// models/account.model.ts
export interface Account {
  accountId: string;
  accountName: string;
  accountType: AccountType;
  balance: number;
  currency: string;
  createdAt: string;
  riskScore: number;
}

export type AccountType = 'PERSONAL' | 'BUSINESS' | 'OFFSHORE';

// models/transaction.model.ts
export interface Transaction {
  transactionId: string;
  fromAccountId: string;
  toAccountId: string;
  amount: number;
  currency: string;
  timestamp: string;
  description: string;
  ruleMatches: RuleMatch[];
}

// models/rule-match.model.ts
export interface RuleMatch {
  ruleId: string;
  ruleName: string;
  severity: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  description: string;
  matchedAt: string;
}
```

## Rendering Account Networks (No Graph Library)

### Table-Based Network Display

```html
<!-- account-network.component.html -->
<div class="network-container">
  <h2>Account Network</h2>
  
  <table class="network-table">
    <thead>
      <tr>
        <th>Account</th>
        <th>Type</th>
        <th>Risk Score</th>
        <th>Connected Accounts</th>
        <th>Actions</th>
      </tr>
    </thead>
    <tbody>
      @for (account of accounts$ | async; track account.accountId) {
        <tr [class.high-risk]="account.riskScore > 70">
          <td>{{ account.accountName }}</td>
          <td>
            <span class="badge" [attr.data-type]="account.accountType">
              {{ account.accountType }}
            </span>
          </td>
          <td>
            <app-risk-indicator [score]="account.riskScore" />
          </td>
          <td>{{ account.connectedAccounts?.length || 0 }}</td>
          <td>
            <button (click)="onViewDetails(account.accountId)">View</button>
          </td>
        </tr>
      }
    </tbody>
  </table>
</div>
```

### Card-Based Account Display

```html
<!-- account-card.component.html -->
<div class="account-card" [class.flagged]="account().ruleMatches?.length > 0">
  <header class="card-header">
    <h3>{{ account().accountName }}</h3>
    <span class="account-type">{{ account().accountType }}</span>
  </header>
  
  <div class="card-body">
    <div class="stat">
      <label>Balance</label>
      <span>{{ account().balance | currency:account().currency }}</span>
    </div>
    <div class="stat">
      <label>Risk Score</label>
      <div class="risk-bar">
        <div class="risk-fill" [style.width.%]="account().riskScore"></div>
      </div>
      <span>{{ account().riskScore }}/100</span>
    </div>
  </div>
  
  @if (account().ruleMatches?.length) {
    <footer class="card-footer">
      <div class="rule-badges">
        @for (match of account().ruleMatches; track match.ruleId) {
          <span class="rule-badge" [attr.data-severity]="match.severity">
            {{ match.ruleName }}
          </span>
        }
      </div>
    </footer>
  }
</div>
```

## Rule Match Badge Directive

```typescript
// shared/directives/rule-severity.directive.ts
import { Directive, ElementRef, input, effect, inject } from '@angular/core';

@Directive({
  selector: '[appRuleSeverity]',
  standalone: true
})
export class RuleSeverityDirective {
  private el = inject(ElementRef);
  
  severity = input.required<string>({ alias: 'appRuleSeverity' });
  
  constructor() {
    effect(() => {
      const severityClass = `severity-${this.severity().toLowerCase()}`;
      this.el.nativeElement.classList.add(severityClass);
    });
  }
}
```

## Routing Configuration

```typescript
// app.routes.ts
import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./features/aml-dashboard/aml-dashboard.component')
      .then(m => m.AmlDashboardComponent),
    title: 'AML Dashboard'
  },
  {
    path: 'patterns',
    loadComponent: () => import('./features/pattern-viewer/pattern-viewer.component')
      .then(m => m.PatternViewerComponent),
    title: 'Pattern Viewer'
  },
  {
    path: 'network',
    loadComponent: () => import('./features/account-network/account-network.component')
      .then(m => m.AccountNetworkComponent),
    title: 'Account Network'
  },
  {
    path: 'network/:accountId',
    loadComponent: () => import('./features/account-network/components/account-detail/account-detail.component')
      .then(m => m.AccountDetailComponent),
    title: 'Account Details'
  }
];
```

## Security & Performance

- Sanitize all user inputs before display
- Use `trackBy` functions in all `@for` loops
- Implement lazy loading for feature modules
- Use `OnPush` change detection for presentational components
- Cache HTTP responses where appropriate using RxJS operators
- Implement proper error boundaries

## Testing Conventions

- Unit tests for services with mocked HTTP client
- Component tests for presentational components
- Integration tests for smart components with service mocks
- Use `TestBed` with standalone component imports

```typescript
// account.service.spec.ts
describe('AccountService', () => {
  let service: AccountService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AccountService]
    });
    service = TestBed.inject(AccountService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should fetch accounts', () => {
    const mockAccounts: Account[] = [/* test data */];
    
    service.getAccounts().subscribe(accounts => {
      expect(accounts).toEqual(mockAccounts);
    });

    const req = httpMock.expectOne('/api/accounts');
    expect(req.request.method).toBe('GET');
    req.flush({ data: mockAccounts, meta: {}, errors: [] });
  });
});
```

