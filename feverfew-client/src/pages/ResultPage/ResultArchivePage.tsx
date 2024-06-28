import { useParams } from 'react-router-dom';
import { Container, Stack } from '@mantine/core';
import { useDocumentTitle } from '@mantine/hooks';
import { useGetOneCheck } from '@/hooks/useGetOneCheck';
import { ResultHeader } from '@/pages/ResultPage/components/ResultHeader';
import { ResultIndicators } from '@/pages/ResultPage/components/ResultIndicators';
import { ResultItem } from '@/pages/ResultPage/components/ResultItem';
import { ResultError } from '@/pages/ResultPage/components/ResultError';
import { ResultSkeleton } from '@/pages/ResultPage/components/ResultSkeleton';
import { ReviewActionButton } from '@/pages/ResultPage/components/ReviewActionButton';
import { ReviewPanel } from '@/pages/ResultPage/components/ReviewPanel';
import { useNavigateResults } from '@/hooks/useNavigateResults';

export function ResultArchivePage() {
  const { id } = useParams();

  const { data: response, isSuccess, isError } = useGetOneCheck(id!);

  useDocumentTitle(
    response ? `[${response.data.wikiId}] ${response.data.pageTitle} – Feverfew` : 'Feverfew'
  );

  useNavigateResults();

  return (
    <Container size="xl">
      {isSuccess ? (
        <>
          <Stack my="md" gap="sm">
            <ResultHeader response={response.data} />

            <ResultIndicators response={response.data} />

            <Stack gap="xs">
              {response.data.results.map((result) => (
                <ResultItem key={result.link.id} result={result} />
              ))}
            </Stack>
          </Stack>
          <ReviewActionButton />
          <ReviewPanel wikiId={response.data.wikiId} revisionId={response.data.pageRevisionId} />
        </>
      ) : isError ? (
        <ResultError />
      ) : (
        <ResultSkeleton />
      )}
    </Container>
  );
}
