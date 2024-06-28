import { SimpleGrid, Skeleton, Stack } from '@mantine/core';

export function ResultSkeleton() {
  return (
    <Stack my="md" gap="sm">
      <Skeleton height={85} radius="md" />

      <SimpleGrid
        cols={{
          base: 2,
          md: 4,
        }}
        spacing="sm"
        verticalSpacing="sm"
      >
        <Skeleton height={50} radius="md" />
        <Skeleton height={50} radius="md" />
        <Skeleton height={50} radius="md" />
        <Skeleton height={50} radius="md" />
      </SimpleGrid>

      <Stack gap="xs">
        {Array(4)
          .fill(0)
          .map((_, index) => (
            <Skeleton key={index} height={85} radius="md" />
          ))}
      </Stack>
    </Stack>
  );
}
