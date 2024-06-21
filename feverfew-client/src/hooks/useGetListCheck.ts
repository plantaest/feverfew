import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { AppResponse } from '@/types/api/AppResponse';
import { ListResponse } from '@/types/api/ListResponse';
import { GetListCheckResponse } from '@/types/api/Check';
import { apiServerUri } from '@/utils/apiServerUri';
import { AppError } from '@/types/api/AppError';
import { appConfig } from '@/config/appConfig';

const getListCheck = async (
  page: number,
  size: number
): Promise<AppResponse<ListResponse<GetListCheckResponse>>> => {
  const url = apiServerUri(`check?page=${page}&size=${size}`);
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

export function useGetListCheck(page: number, size: number) {
  return useQuery<AppResponse<ListResponse<GetListCheckResponse>>, AppError>({
    queryKey: [
      'check',
      'getList',
      {
        page,
        size,
      },
    ],
    queryFn: () => getListCheck(page, size),
    placeholderData: keepPreviousData,
  });
}
