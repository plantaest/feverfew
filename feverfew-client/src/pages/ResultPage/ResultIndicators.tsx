import { Group, SimpleGrid, Text } from '@mantine/core';
import clsx from 'clsx';
import { useTranslation } from 'react-i18next';
import classes from '@/pages/ResultPage/ResultPage.module.css';
import { CreateCheckResponse, GetOneCheckResponse } from '@/types/api/Check';

interface ResultIndicatorsProps {
  response: GetOneCheckResponse | CreateCheckResponse;
}

export function ResultIndicators({ response }: ResultIndicatorsProps) {
  const { t } = useTranslation();

  return (
    <SimpleGrid
      cols={{
        base: 2,
        md: 4,
      }}
      spacing="sm"
      verticalSpacing="sm"
    >
      <Group className={clsx(classes.linkNumber, classes.totalBg)}>
        <Text>{t('core:ui.result.total')}</Text>
        <Text fw={600}>{response.totalLinks}</Text>
      </Group>
      <Group className={clsx(classes.linkNumber, classes.ignoredBg)}>
        <Text>{t('core:ui.result.ignored')}</Text>
        <Text fw={600}>{response.totalIgnoredLinks}</Text>
      </Group>
      <Group className={clsx(classes.linkNumber, classes.workingBg)}>
        <Text>{t('core:ui.result.working')}</Text>
        <Text fw={600}>{response.totalWorkingLinks}</Text>
      </Group>
      <Group className={clsx(classes.linkNumber, classes.brokenBg)}>
        <Text>{t('core:ui.result.broken')}</Text>
        <Text fw={600}>{response.totalBrokenLinks}</Text>
      </Group>
    </SimpleGrid>
  );
}
