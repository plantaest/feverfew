import { useTranslation } from 'react-i18next';
import { Stack, Text } from '@mantine/core';
import { IconAlertTriangle } from '@tabler/icons-react';

export function ResultError() {
  const { t } = useTranslation();

  return (
    <Stack mt={60} mb="md" c="pink" align="center">
      <IconAlertTriangle size="8rem" stroke={1} />
      <Text fw={600} fz="xl" ff="var(--mantine-alt-font-family)">
        {t('core:ui.result.errorMessage')}
      </Text>
    </Stack>
  );
}
