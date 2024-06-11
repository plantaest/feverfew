import { useParams } from 'react-router-dom';
import { Container, Stack } from '@mantine/core';
import { useDocumentTitle } from '@mantine/hooks';
import { useGetOneCheck } from '@/hooks/useGetOneCheck';
import { ResultHeader } from '@/pages/ResultPage/ResultHeader';
import { ResultIndicators } from '@/pages/ResultPage/ResultIndicators';
import { ResultItem } from '@/pages/ResultPage/ResultItem';
import { ResultError } from '@/pages/ResultPage/ResultError';
import { ResultSkeleton } from '@/pages/ResultPage/ResultSkeleton';

export function ResultArchivePage() {
  const { id } = useParams();

  const { data: response, isSuccess, isError } = useGetOneCheck(id!);

  useDocumentTitle(
    response ? `[${response.data.wikiId}] ${response.data.pageTitle} – Feverfew` : 'Feverfew'
  );

  return (
    <Container size="xl">
      {isSuccess ? (
        <Stack my="md" gap="sm">
          <ResultHeader response={response.data} />

          <ResultIndicators response={response.data} />

          <Stack gap="xs">
            {response.data.results.map((result) => (
              <ResultItem key={result.link.id} result={result} />
            ))}
          </Stack>
        </Stack>
      ) : isError ? (
        <ResultError />
      ) : (
        <ResultSkeleton />
      )}
    </Container>
  );
}
