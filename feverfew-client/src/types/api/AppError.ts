export interface AppError {
  status: number;
  type: string;
  title: string;
  detail: string;
  instance: string;
  code: string;
  errorId: string;
  violations: Violation[];
}

interface Violation {
  field: string;
  messages: string[];
}
