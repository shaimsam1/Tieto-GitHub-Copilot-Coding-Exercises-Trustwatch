import { AmlFlag } from './aml-flag.model';

// Account model
export interface Account {
  accountId: string;
  customerName: string;
  iban: string;
  accountType: AccountType;
  openedDate: string;
  riskRating: RiskRating;
  amlFlags: AmlFlag[];
}

export type AccountType = 'PERSONAL' | 'BUSINESS' | 'OFFSHORE';

export type RiskRating = 'LOW' | 'MEDIUM' | 'HIGH';
