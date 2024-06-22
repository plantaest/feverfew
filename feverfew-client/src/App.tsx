import '@mantine/core/styles.css';
import '@mantine/notifications/styles.css';
import './index.css';
import { CopyButton, DirectionProvider, Group, MantineProvider, Text } from '@mantine/core';
import { enableReactTracking } from '@legendapp/state/config/enableReactTracking';
import { configureObservablePersistence, persistObservable } from '@legendapp/state/persist';
import { ObservablePersistLocalStorage } from '@legendapp/state/persist-plugins/local-storage';
import { MutationCache, QueryCache, QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Notifications } from '@mantine/notifications';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { IconCopy } from '@tabler/icons-react';
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
          <Group gap={8}>
            <Text size="sm">
              {(query.meta?.errorMessage as string) || i18n.t('core:query.defaultErrorMessage')}
            </Text>
            <CopyButton value={JSON.stringify(error, null, 2)}>
              {({ copied, copy }) => (
                <IconCopy
                  style={{ minWidth: '0.85rem' }}
                  size="0.85rem"
                  color={copied ? 'var(--mantine-color-teal-5)' : 'var(--mantine-color-blue-5)'}
                  onClick={copy}
                />
              )}
            </CopyButton>
          </Group>
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
          <Group gap={8}>
            <Text size="sm">
              {(mutation.meta?.errorMessage as string) || i18n.t('core:query.defaultErrorMessage')}
            </Text>
            <CopyButton value={JSON.stringify(error, null, 2)}>
              {({ copied, copy }) => (
                <IconCopy
                  style={{ minWidth: '0.85rem' }}
                  size="0.85rem"
                  color={copied ? 'var(--mantine-color-teal-5)' : 'var(--mantine-color-blue-5)'}
                  onClick={copy}
                />
              )}
            </CopyButton>
          </Group>
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
