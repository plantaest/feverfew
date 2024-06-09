import '@mantine/core/styles.css';
import '@mantine/notifications/styles.css';
import './index.css';
import { DirectionProvider, MantineProvider } from '@mantine/core';
import { enableReactTracking } from '@legendapp/state/config/enableReactTracking';
import { configureObservablePersistence, persistObservable } from '@legendapp/state/persist';
import { ObservablePersistLocalStorage } from '@legendapp/state/persist-plugins/local-storage';
import { MutationCache, QueryCache, QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Notifications } from '@mantine/notifications';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { theme } from './theme';
import { Router } from './Router';
import { appState } from '@/states/appState';
import { Notify } from '@/utils/Notify';
import { appConfig } from '@/config/appConfig';
import i18n from '@/i18n';

// Legend State Config
enableReactTracking({ auto: true });

configureObservablePersistence({
  pluginLocal: ObservablePersistLocalStorage,
});

persistObservable(appState.local, {
  local: 'feverfew.appState.local',
});

// Query Client
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
    },
  },
  queryCache: new QueryCache({
    onError: (error, query) => {
      if (query.meta?.showErrorNotification !== false) {
        Notify.error(
          (query.meta?.errorMessage as string) || i18n.t('core:query.defaultErrorMessage')
        );
      }

      if (appConfig.DEBUG) {
        // eslint-disable-next-line no-console
        console.error(error);
      }
    },
  }),
  mutationCache: new MutationCache({
    onError: (error, _variables, _context, mutation) => {
      if (mutation.meta?.showErrorNotification !== false) {
        Notify.error(
          (mutation.meta?.errorMessage as string) || i18n.t('core:query.defaultErrorMessage')
        );
      }

      if (appConfig.DEBUG) {
        // eslint-disable-next-line no-console
        console.error(error);
      }
    },
  }),
});

export default function App() {
  const dir = appState.local.dir.get();

  return (
    <QueryClientProvider client={queryClient}>
      <DirectionProvider initialDirection={dir} detectDirection={false}>
        <MantineProvider theme={theme} defaultColorScheme="auto">
          <Notifications />
          <Router />
        </MantineProvider>
      </DirectionProvider>
      <ReactQueryDevtools initialIsOpen={false} buttonPosition="bottom-left" />
    </QueryClientProvider>
  );
}
