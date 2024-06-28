import { observable, ObservableObject } from '@legendapp/state';
import { RefObject } from 'react';
import i18n from '@/i18n';
import { isRtlLang } from '@/utils/isRtlLang';
import { EvaluationResult } from '@/types/api/Check';

interface AppState {
  local: {
    dir: 'ltr' | 'rtl';
    selectedWikiId: string | null;
  };
  review: {
    enabled: boolean;
    selectedResult: EvaluationResult | null;
    resultRefs: Map<number, RefObject<HTMLDivElement>>;
  };
}

export const appState: ObservableObject<AppState> = observable<AppState>({
  local: {
    dir: isRtlLang(i18n.language) ? 'rtl' : 'ltr',
    selectedWikiId: null,
  },
  review: {
    enabled: false,
    selectedResult: null,
    resultRefs: new Map(),
  },
});
