export interface CreateCheckRequest {
  wikiId: string;
  pageTitle: string;
  pageRevisionId: number | null;
}

export interface CreateCheckResponse {
  id: string;
  createdAt: string;
  createdBy: number;
  wikiId: string;
  wikiServerName: string;
  pageTitle: string;
  pageRevisionId: number;
  durationInMillis: number;
  totalLinks: number;
  totalIgnoredLinks: number;
  totalSuccessLinks: number;
  totalErrorLinks: number;
  totalWorkingLinks: number;
  totalBrokenLinks: number;
  results: EvaluationResult[];
}

export interface EvaluationResult {
  index: number;
  link: ExternalLink;
  requestResult: RequestResult;
  classificationResult: ClassificationResult;
}

export interface ExternalLink {
  id: string;
  href: string;
  scheme: string | null;
  host: string;
  port: string | null;
  path: string | null;
  query: string | null;
  fragment: string | null;
  isIPv4: boolean;
  isIPv6: boolean;
  tld: string | null;
  text: string | null;
  fileType: string | null;
  refIndex: number | null;
  refName: string | null;
}

export interface RequestResult {
  type: RequestResultType;
  requestDuration: number;
  responseStatus: number;
  contentType: string | null;
  contentLength: number;
  containsPageNotFoundWords: boolean;
  containsPaywallWords: boolean;
  containsDomainExpiredWords: boolean;
  redirects: Redirect[];
  redirectToHomepage: boolean;
}

export enum RequestResultType {
  SUCCESS = 'SUCCESS',
  ERROR = 'ERROR',
  IGNORED = 'IGNORED',
}

export interface Redirect {
  requestUrl: string;
  location: string;
  responseStatus: number;
}

export interface ClassificationResult {
  label: number;
  probability: number;
}
