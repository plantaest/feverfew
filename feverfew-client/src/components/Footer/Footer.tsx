import { Anchor, Flex, Text } from '@mantine/core';
import { useComputed } from '@legendapp/state/react';
import { appConfig } from '@/config/appConfig';
import { appState } from '@/states/appState';

export function Footer() {
  const paddingBottom = useComputed(() => (appState.review.enabled.get() ? 50 + 550 : 50));

  return (
    <Flex direction="column" pt={50} pb={paddingBottom.get()} ta="center" gap={8}>
      <Text c="dimmed">
        <Anchor href="https://meta.wikimedia.org/wiki/User:Plantaest" target="_blank">
          Plantaest
        </Anchor>{' '}
        @ 2024
      </Text>
      <Text c="dimmed" fz="xs" ff="var(--mantine-alt-font-monospace)">
        {appConfig.VERSION}
      </Text>
    </Flex>
  );
}
