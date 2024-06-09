import { observable, ObservableObject } from '@legendapp/state';
import i18n from '@/i18n';
import { isRtlLang } from '@/utils/isRtlLang';

interface AppState {
  local: {
    dir: 'ltr' | 'rtl';
    selectedWikiId: string | null;
  };
}

export const appState: ObservableObject<AppState> = observable<AppState>({
  local: {
    dir: isRtlLang(i18n.language) ? 'rtl' : 'ltr',
    selectedWikiId: null,
  },
});
