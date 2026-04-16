import { Component, ChangeDetectionStrategy, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransactionStatus } from '@models/index';

@Component({
  selector: 'app-status-badge',
  standalone: true,
  imports: [CommonModule],
  template: `
    <span class="status-badge" [attr.data-status]="status()">
      <span class="status-dot"></span>
      {{ formatStatus() }}
    </span>
  `,
  styles: [`
    .status-badge {
      display: inline-flex;
      align-items: center;
      gap: 0.375rem;
      padding: 0.25rem 0.625rem;
      font-size: 0.75rem;
      font-weight: 500;
      border-radius: 9999px;
    }

    .status-dot {
      width: 6px;
      height: 6px;
      border-radius: 50%;
    }

    .status-badge[data-status='APPROVED'] {
      background: #dcfce7;
      color: #166534;
    }

    .status-badge[data-status='APPROVED'] .status-dot {
      background: #22c55e;
    }

    .status-badge[data-status='PENDING_REVIEW'] {
      background: #fef3c7;
      color: #92400e;
    }

    .status-badge[data-status='PENDING_REVIEW'] .status-dot {
      background: #f59e0b;
      animation: blink 1.5s infinite;
    }

    .status-badge[data-status='BLOCKED'] {
      background: #fee2e2;
      color: #991b1b;
    }

    .status-badge[data-status='BLOCKED'] .status-dot {
      background: #ef4444;
    }

    @keyframes blink {
      0%, 100% { opacity: 1; }
      50% { opacity: 0.4; }
    }
  `],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class StatusBadgeComponent {
  status = input.required<TransactionStatus>();

  formatStatus(): string {
    return this.status().replace(/_/g, ' ');
  }
}
