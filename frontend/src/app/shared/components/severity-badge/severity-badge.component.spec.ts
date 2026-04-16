import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SeverityBadgeComponent } from './severity-badge.component';

describe('SeverityBadgeComponent', () => {
  let component: SeverityBadgeComponent;
  let fixture: ComponentFixture<SeverityBadgeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SeverityBadgeComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(SeverityBadgeComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.componentRef.setInput('severity', 'MEDIUM');
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should display the severity text', () => {
    fixture.componentRef.setInput('severity', 'CRITICAL');
    fixture.detectChanges();
    
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('CRITICAL');
  });

  it('should apply correct data attribute for severity', () => {
    fixture.componentRef.setInput('severity', 'HIGH');
    fixture.detectChanges();
    
    const badge = fixture.nativeElement.querySelector('.severity-badge');
    expect(badge.getAttribute('data-severity')).toBe('HIGH');
  });
});
