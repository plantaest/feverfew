import { Container, Stack } from '@mantine/core';
import { useSearchParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { useDocumentTitle } from '@mantine/hooks';
import { useTranslation } from 'react-i18next';
import { CreateCheckResponse } from '@/types/api/Check';
import { useCreateCheck } from '@/hooks/useCreateCheck';
import { ResultError } from './ResultError';
import { ResultSkeleton } from './ResultSkeleton';
import { ResultList } from './ResultList';
import { ResultHeader } from '@/pages/ResultPage/ResultHeader';
import { ResultIndicators } from '@/pages/ResultPage/ResultIndicators';
import { ResultItem } from '@/pages/ResultPage/ResultItem';

export function ResultPage() {
  const { t } = useTranslation();
  const [searchParams] = useSearchParams();

  const wikiId = searchParams.get('wiki')?.trim() || null;
  const pageTitle = searchParams.get('page')?.replaceAll('_', ' ').trim() || null;

  const shouldCheck = wikiId && pageTitle;

  const [response, setResponse] = useState<CreateCheckResponse | null>(null);

  const createCheckApi = useCreateCheck();

  useDocumentTitle(
    shouldCheck
      ? `[${wikiId}] ${response ? response.pageTitle : pageTitle} – Feverfew`
      : `${t('core:ui.result.list.title')} – Feverfew`
  );

  useEffect(() => {
    if (shouldCheck) {
      createCheckApi.mutate(
        {
          wikiId: wikiId,
          pageTitle: pageTitle,
          pageRevisionId: null,
        },
        {
          onSuccess: (r) => setResponse(r.data),
        }
      );
    }
  }, [shouldCheck]);

  if (!shouldCheck) {
    return <ResultList />;
  }

  return (
    <Container size="xl">
      {createCheckApi.isSuccess && response ? (
        <Stack my="md" gap="sm">
          <ResultHeader response={response} />

          <ResultIndicators response={response} />

          <Stack gap="xs">
            {response.results.map((result) => (
              <ResultItem key={result.link.id} result={result} />
            ))}
          </Stack>
        </Stack>
      ) : createCheckApi.isError ? (
        <ResultError />
      ) : (
        <ResultSkeleton />
      )}
    </Container>
  );
}
