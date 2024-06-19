import { Anchor, Flex, Text } from '@mantine/core';
import { appConfig } from '@/config/appConfig';

export function Footer() {
  return (
    <Flex direction="column" py={50} ta="center" gap={8}>
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
