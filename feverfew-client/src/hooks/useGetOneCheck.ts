import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { AppResponse } from '@/types/api/AppResponse';
import { GetOneCheckResponse } from '@/types/api/Check';
import { apiServerUri } from '@/utils/apiServerUri';
import { AppError } from '@/types/api/AppError';

const getOneCheck = async (id: string): Promise<AppResponse<GetOneCheckResponse>> => {
  const url = apiServerUri(`check/${id}`);
  const options = {
    method: 'GET',
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

export function useGetOneCheck(id: string) {
  return useQuery<AppResponse<GetOneCheckResponse>, AppError>({
    queryKey: ['check', 'getOne', id],
    queryFn: () => getOneCheck(id),
    placeholderData: keepPreviousData,
  });
}
