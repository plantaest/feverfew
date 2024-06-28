import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { AppResponse } from '@/types/api/AppResponse';
import { apiServerUri } from '@/utils/apiServerUri';
import { appConfig } from '@/config/appConfig';
import { GetRevisionWikitextResponse } from '@/types/api/Wiki';
import { AppError } from '@/types/api/AppError';

const getRevisionWikitext = async (
  wikiId: string,
  revisionId: number
): Promise<AppResponse<GetRevisionWikitextResponse>> => {
  const url = apiServerUri(`wiki/revision/wikitext?wikiId=${wikiId}&revisionId=${revisionId}`);
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

export function useGetRevisionWikitext(wikiId: string, revisionId: number) {
  return useQuery<AppResponse<GetRevisionWikitextResponse>, AppError>({
    queryKey: [
      'wiki',
      'revision',
      'wikitext',
      {
        wikiId,
        revisionId,
      },
    ],
    queryFn: () => getRevisionWikitext(wikiId, revisionId),
    placeholderData: keepPreviousData,
    staleTime: Infinity,
  });
}
