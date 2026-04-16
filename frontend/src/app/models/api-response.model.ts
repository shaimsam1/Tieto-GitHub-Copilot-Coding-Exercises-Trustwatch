// TypeScript models for AML Pattern Detection Dashboard

// API Response envelope
export interface ApiResponse<T> {
  data: T;
  meta: ApiMeta;
  errors: ApiError[];
}

export interface ApiMeta {
  timestamp: string;
  requestId: string;
}

export interface ApiError {
  code: string;
  message: string;
  field?: string;
}
