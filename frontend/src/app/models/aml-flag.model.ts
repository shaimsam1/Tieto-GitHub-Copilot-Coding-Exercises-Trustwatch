// AML Flag model
export interface AmlFlag {
  flagId: string;
  accountId?: string;
  typology: AmlTypology;
  confidenceScore: number;
  severity: Severity;
  detectedAt: string;
  recommendedAction: RecommendedAction;
}

export type AmlTypology =
  | 'STRUCTURING'
  | 'LAYERING'
  | 'ROUND_TRIPPING'
  | 'SHELL_NETWORK'
  | 'VELOCITY_ANOMALY'
  | 'SMURFING';

export type Severity = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';

export type RecommendedAction =
  | 'MONITOR'
  | 'EDD'
  | 'SAR'
  | 'FREEZE'
  | 'FIU_ESCALATION';
