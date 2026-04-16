import { Component, ChangeDetectionStrategy, input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-risk-indicator',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="risk-indicator" [attr.data-level]="getRiskLevel()">
      <div class="risk-bar">
        <div class="risk-fill" [style.width.%]="score()"></div>
      </div>
      <span class="risk-label">{{ score() }}/100</span>
    </div>
  `,
  styles: [`
    .risk-indicator {
      display: flex;
      align-items: center;
      gap: 0.5rem;
    }

    .risk-bar {
      width: 60px;
      height: 8px;
      background: #e5e7eb;
      border-radius: 4px;
      overflow: hidden;
    }

    .risk-fill {
      height: 100%;
      border-radius: 4px;
      transition: width 0.3s ease;
    }

    .risk-indicator[data-level='low'] .risk-fill {
      background: linear-gradient(90deg, #22c55e, #4ade80);
    }

    .risk-indicator[data-level='medium'] .risk-fill {
      background: linear-gradient(90deg, #f59e0b, #fbbf24);
    }

    .risk-indicator[data-level='high'] .risk-fill {
      background: linear-gradient(90deg, #ef4444, #f87171);
    }

    .risk-label {
      font-size: 0.75rem;
      font-weight: 500;
      color: #6b7280;
      min-width: 45px;
    }
  `],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RiskIndicatorComponent {
  score = input.required<number>();

  getRiskLevel(): string {
    const s = this.score();
    if (s < 40) return 'low';
    if (s < 70) return 'medium';
    return 'high';
  }
}
