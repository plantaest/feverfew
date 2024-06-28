import { Skeleton, Stack } from '@mantine/core';

export function ResultListItemsSkeleton() {
  return (
    <Stack gap="xs">
      <Skeleton height={22} width={110} radius="md" />
      {Array(7)
        .fill(0)
        .map((_, index) => (
          <Skeleton key={index} height={75} radius="md" />
        ))}
    </Stack>
  );
}
