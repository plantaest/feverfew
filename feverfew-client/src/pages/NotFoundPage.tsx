import { Container, Text } from '@mantine/core';

export function NotFoundPage() {
  return (
    <Container size="xl" py={80}>
      <Text ta="center" fw={600} fz={75} c="gray">
        404
      </Text>
    </Container>
  );
}
