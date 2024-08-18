/**
 * getAllStats
 */
export interface GetAllStatsResponse {
  totalChecks: number;
  totalAnonymousUsers: number;
  totalWikis: number;
  totalPages: number;
  totalDurationInMillis: number;
  totalLinks: number;
  totalIgnoredLinks: number;
  totalWorkingLinks: number;
  totalBrokenLinks: number;
}
