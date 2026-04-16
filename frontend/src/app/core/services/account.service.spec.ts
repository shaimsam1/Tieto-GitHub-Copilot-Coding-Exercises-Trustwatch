import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AccountService } from './account.service';
import { Account } from '@models/index';

describe('AccountService', () => {
  let service: AccountService;
  let httpMock: HttpTestingController;

  const mockAccounts: Account[] = [
    {
      accountId: 'acct-001',
      customerName: 'Test User',
      iban: 'FI1234567890',
      accountType: 'PERSONAL',
      openedDate: '2023-01-01',
      riskRating: 'LOW',
      amlFlags: []
    },
    {
      accountId: 'acct-002',
      customerName: 'Business Corp',
      iban: 'DE9876543210',
      accountType: 'BUSINESS',
      openedDate: '2022-06-15',
      riskRating: 'HIGH',
      amlFlags: [
        {
          flagId: 'flag-001',
          typology: 'STRUCTURING',
          confidenceScore: 85,
          severity: 'HIGH',
          detectedAt: '2026-04-10T08:00:00Z',
          recommendedAction: 'SAR'
        }
      ]
    }
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AccountService]
    });

    service = TestBed.inject(AccountService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all accounts', (done) => {
    service.getAccounts().subscribe({
      next: (accounts) => {
        expect(accounts).toEqual(mockAccounts);
        expect(accounts.length).toBe(2);
        done();
      }
    });

    const req = httpMock.expectOne('http://localhost:8080/api/accounts');
    expect(req.request.method).toBe('GET');
    req.flush({
      data: mockAccounts,
      meta: { timestamp: '2026-04-16T10:00:00Z', requestId: 'test-123' },
      errors: []
    });
  });

  it('should fetch account by ID', (done) => {
    const accountId = 'acct-001';

    service.getAccountById(accountId).subscribe({
      next: (account) => {
        expect(account).toEqual(mockAccounts[0]);
        expect(account.accountId).toBe(accountId);
        done();
      }
    });

    const req = httpMock.expectOne(`http://localhost:8080/api/accounts/${accountId}`);
    expect(req.request.method).toBe('GET');
    req.flush({
      data: mockAccounts[0],
      meta: { timestamp: '2026-04-16T10:00:00Z', requestId: 'test-456' },
      errors: []
    });
  });

  it('should update selected account on selectAccount', () => {
    const account = mockAccounts[0];
    
    service.selectAccount(account);
    
    service.selectedAccount$.subscribe(selected => {
      expect(selected).toEqual(account);
    });
  });

  it('should clear selected account on clearSelection', () => {
    service.selectAccount(mockAccounts[0]);
    service.clearSelection();
    
    service.selectedAccount$.subscribe(selected => {
      expect(selected).toBeNull();
    });
  });
});
