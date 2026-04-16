import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RiskIndicatorComponent } from './risk-indicator.component';

describe('RiskIndicatorComponent', () => {
  let component: RiskIndicatorComponent;
  let fixture: ComponentFixture<RiskIndicatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RiskIndicatorComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(RiskIndicatorComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.componentRef.setInput('score', 50);
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should return low risk level for scores below 40', () => {
    fixture.componentRef.setInput('score', 30);
    fixture.detectChanges();
    expect(component.getRiskLevel()).toBe('low');
  });

  it('should return medium risk level for scores between 40 and 69', () => {
    fixture.componentRef.setInput('score', 55);
    fixture.detectChanges();
    expect(component.getRiskLevel()).toBe('medium');
  });

  it('should return high risk level for scores 70 and above', () => {
    fixture.componentRef.setInput('score', 85);
    fixture.detectChanges();
    expect(component.getRiskLevel()).toBe('high');
  });

  it('should display correct score in template', () => {
    fixture.componentRef.setInput('score', 75);
    fixture.detectChanges();
    
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('75/100');
  });
});
