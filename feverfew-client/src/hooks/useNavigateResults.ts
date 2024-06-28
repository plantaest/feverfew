import { useHotkeys } from '@mantine/hooks';
import { useEffect } from 'react';
import { appState } from '@/states/appState';

export function useNavigateResults() {
  const clickSelectedResult = () => {
    const selectedResult = appState.review.selectedResult.get();
    const resultRefs = appState.review.resultRefs.peek();

    if (selectedResult) {
      resultRefs.get(selectedResult.index)?.current?.click();
    }
  };

  const clickPreviousResult = () => {
    const selectedResult = appState.review.selectedResult.get();
    const resultRefs = appState.review.resultRefs.peek();

    if (!selectedResult) {
      resultRefs.get(1)?.current?.click();
    } else {
      resultRefs.get(selectedResult.index - 1)?.current?.click();
    }
  };

  const clickNextResult = () => {
    const selectedResult = appState.review.selectedResult.get();
    const resultRefs = appState.review.resultRefs.peek();

    if (!selectedResult) {
      resultRefs.get(1)?.current?.click();
    } else {
      resultRefs.get(selectedResult.index + 1)?.current?.click();
    }
  };

  useHotkeys([
    ['Q', clickSelectedResult],
    ['A', clickPreviousResult],
    ['Z', clickNextResult],
  ]);

  useEffect(
    () => () => {
      appState.review.enabled.set(false);
      appState.review.selectedResult.set(null);
      appState.review.resultRefs.peek().clear();
    },
    []
  );
}
