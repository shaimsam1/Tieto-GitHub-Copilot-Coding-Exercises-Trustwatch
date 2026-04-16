import { Component, ChangeDetectionStrategy, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountType } from '@models/index';

@Component({
  selector: 'app-account-type-badge',
  standalone: true,
  imports: [CommonModule],
  template: `
    <span class="account-type-badge" [attr.data-type]="type()">
      {{ type() }}
    </span>
  `,
  styles: [`
    .account-type-badge {
      display: inline-flex;
      align-items: center;
      padding: 0.25rem 0.5rem;
      font-size: 0.625rem;
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: 0.05em;
      border-radius: 4px;
    }

    .account-type-badge[data-type='PERSONAL'] {
      background: #dbeafe;
      color: #1e40af;
    }

    .account-type-badge[data-type='BUSINESS'] {
      background: #e0e7ff;
      color: #3730a3;
    }

    .account-type-badge[data-type='OFFSHORE'] {
      background: #fce7f3;
      color: #9d174d;
    }
  `],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AccountTypeBadgeComponent {
  type = input.required<AccountType>();
}
