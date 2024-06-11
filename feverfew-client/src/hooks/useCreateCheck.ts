import { useMutation, useQueryClient } from '@tanstack/react-query';
import { AppResponse } from '@/types/api/AppResponse';
import { CreateCheckRequest, CreateCheckResponse } from '@/types/api/Check';
import { AppError } from '@/types/api/AppError';
import { apiServerUri } from '@/utils/apiServerUri';

const createCheck = async (
  request: CreateCheckRequest
): Promise<AppResponse<CreateCheckResponse>> => {
  const url = apiServerUri('check/create');
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(request),
  };

  const response = await fetch(url, options);

  if (!response.ok) {
    throw await response.json();
  }

  return response.json();
};

export function useCreateCheck() {
  const queryClient = useQueryClient();

  return useMutation<AppResponse<CreateCheckResponse>, AppError, CreateCheckRequest>({
    mutationKey: ['check', 'create'],
    mutationFn: (request) => createCheck(request),
    onSuccess: () =>
      queryClient.invalidateQueries({
        queryKey: ['check', 'getList'],
        refetchType: 'all',
      }),
  });
}
