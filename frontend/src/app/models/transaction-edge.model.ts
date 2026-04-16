// Transaction Edge model for account network visualization
export interface TransactionEdge {
  edgeId: string;
  fromAccountId: string;
  toAccountId: string;
  amount: number;
  currency: string;
  hops: number;
  timestamp: string;
  isFlagged: boolean;
}
