import { Anchor, Text } from '@mantine/core';

export function Footer() {
  return (
    <Text c="dimmed" ta="center" my={50}>
      <Anchor href="https://meta.wikimedia.org/wiki/User:Plantaest" target="_blank">
        Plantaest
      </Anchor>{' '}
      @ 2024
    </Text>
  );
}
