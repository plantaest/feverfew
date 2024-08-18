import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { apiServerUri } from '@/utils/apiServerUri';
import { appConfig } from '@/config/appConfig';
import { GetAllStatsResponse } from '@/types/api/Stats';
import { AppResponse } from '@/types/api/AppResponse';
import { AppError } from '@/types/api/AppError';

const getAllStats = async () => {
  const url = apiServerUri('stats/all');
  const options: RequestInit = {
    method: 'GET',
    credentials: appConfig.DEBUG ? 'include' : 'same-origin',
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const response = await fetch(url, options);

  if (!response.ok) {
    throw await response.json();
  }

  return response.json();
};

export function useGetAllStats() {
  return useQuery<AppResponse<GetAllStatsResponse>, AppError>({
    queryKey: ['wiki', 'stats', 'all'],
    queryFn: () => getAllStats(),
    placeholderData: keepPreviousData,
  });
}
