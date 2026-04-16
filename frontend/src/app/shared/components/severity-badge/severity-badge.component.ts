import { Component, ChangeDetectionStrategy, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Severity } from '@models/index';

@Component({
  selector: 'app-severity-badge',
  standalone: true,
  imports: [CommonModule],
  template: `
    <span class="severity-badge" [attr.data-severity]="severity()">
      {{ severity() }}
    </span>
  `,
  styles: [`
    .severity-badge {
      display: inline-flex;
      align-items: center;
      padding: 0.25rem 0.625rem;
      font-size: 0.75rem;
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: 0.025em;
      border-radius: 9999px;
    }

    .severity-badge[data-severity='LOW'] {
      background: #dcfce7;
      color: #166534;
    }

    .severity-badge[data-severity='MEDIUM'] {
      background: #fef3c7;
      color: #92400e;
    }

    .severity-badge[data-severity='HIGH'] {
      background: #fee2e2;
      color: #991b1b;
    }

    .severity-badge[data-severity='CRITICAL'] {
      background: #7f1d1d;
      color: #ffffff;
      animation: pulse 2s infinite;
    }

    @keyframes pulse {
      0%, 100% { opacity: 1; }
      50% { opacity: 0.7; }
    }
  `],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SeverityBadgeComponent {
  severity = input.required<Severity>();
}
