// Transaction model
export interface Transaction {
  transactionId: string;
  accountId: string;
  customerName: string;
  amount: number;
  currency: string;
  merchantName: string;
  merchantCategory: MerchantCategory;
  originCountry: string;
  destinationCountry: string;
  riskScore: number;
  fraudIndicators: FraudIndicator[];
  timestamp: string;
  status: TransactionStatus;
}

export type MerchantCategory =
  | 'CRYPTO_EXCHANGE'
  | 'GAMBLING'
  | 'RETAIL'
  | 'TRAVEL'
  | 'UTILITIES'
  | 'FINANCIAL_SERVICES'
  | 'OTHER';

export type FraudIndicator =
  | 'VELOCITY_ANOMALY'
  | 'UNUSUAL_MERCHANT'
  | 'HIGH_VALUE_SPIKE'
  | 'GEO_ANOMALY'
  | 'STRUCTURING';

export type TransactionStatus = 'APPROVED' | 'PENDING_REVIEW' | 'BLOCKED';
