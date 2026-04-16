import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AmlFlagService } from './aml-flag.service';
import { AmlFlag } from '@models/index';

describe('AmlFlagService', () => {
  let service: AmlFlagService;
  let httpMock: HttpTestingController;

  const mockFlags: AmlFlag[] = [
    {
      flagId: 'flag-001',
      accountId: 'acct-001',
      typology: 'STRUCTURING',
      confidenceScore: 85,
      severity: 'HIGH',
      detectedAt: '2026-04-10T08:00:00Z',
      recommendedAction: 'SAR'
    },
    {
      flagId: 'flag-002',
      accountId: 'acct-002',
      typology: 'LAYERING',
      confidenceScore: 92,
      severity: 'CRITICAL',
      detectedAt: '2026-04-11T05:30:00Z',
      recommendedAction: 'FIU_ESCALATION'
    }
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AmlFlagService]
    });

    service = TestBed.inject(AmlFlagService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all flags', (done) => {
    service.getFlags().subscribe({
      next: (flags) => {
        expect(flags).toEqual(mockFlags);
        expect(flags.length).toBe(2);
        done();
      }
    });

    const req = httpMock.expectOne('http://localhost:8080/api/aml-flags');
    expect(req.request.method).toBe('GET');
    req.flush({
      data: mockFlags,
      meta: { timestamp: '2026-04-16T10:00:00Z', requestId: 'test-789' },
      errors: []
    });
  });

  it('should fetch critical flags', (done) => {
    const criticalFlags = mockFlags.filter(f => f.severity === 'CRITICAL');

    service.getCriticalFlags().subscribe({
      next: (flags) => {
        expect(flags).toEqual(criticalFlags);
        done();
      }
    });

    const req = httpMock.expectOne('http://localhost:8080/api/aml-flags?severity=CRITICAL');
    expect(req.request.method).toBe('GET');
    req.flush({
      data: criticalFlags,
      meta: { timestamp: '2026-04-16T10:00:00Z', requestId: 'test-101' },
      errors: []
    });
  });

  it('should fetch flags by account', (done) => {
    const accountId = 'acct-001';
    const accountFlags = mockFlags.filter(f => f.accountId === accountId);

    service.getFlagsByAccount(accountId).subscribe({
      next: (flags) => {
        expect(flags).toEqual(accountFlags);
        done();
      }
    });

    const req = httpMock.expectOne(`http://localhost:8080/api/aml-flags?accountId=${accountId}`);
    expect(req.request.method).toBe('GET');
    req.flush({
      data: accountFlags,
      meta: { timestamp: '2026-04-16T10:00:00Z', requestId: 'test-102' },
      errors: []
    });
  });
});
