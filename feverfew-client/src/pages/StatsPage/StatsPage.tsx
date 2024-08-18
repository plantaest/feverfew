import { useDocumentTitle } from '@mantine/hooks';
import { Box, Container, SimpleGrid, Stack, Text, Title } from '@mantine/core';
import { useTranslation } from 'react-i18next';
import classes from './StatsPage.module.css';
import { useGetAllStats } from '@/hooks/useGetAllStats';
import { numberFormat } from '@/utils/numberFormat';

export function StatsPage() {
  const { t } = useTranslation();

  useDocumentTitle(`${t('core:ui.header.stats')} – Feverfew`);

  const { data: response, isSuccess } = useGetAllStats();

  return (
    <Container size="xl">
      <Stack my="md" gap="lg">
        <Title order={2}>{t('core:ui.header.stats')}</Title>
        <SimpleGrid
          cols={{
            base: 1,
            sm: 2,
            md: 3,
          }}
        >
          <Box className={classes.box}>
            <Stack gap={5}>
              <Text size="xl" fw={600}>
                {isSuccess ? numberFormat.format(response.data.totalChecks) : 'N/A'}
              </Text>
              <Text>Checks</Text>
            </Stack>
          </Box>
          <Box className={classes.box}>
            <Stack gap={5}>
              <Text size="xl" fw={600}>
                {isSuccess ? numberFormat.format(response.data.totalAnonymousUsers) : 'N/A'}
              </Text>
              <Text>Anonymous users</Text>
            </Stack>
          </Box>
          <Box className={classes.box}>
            <Stack gap={5}>
              <Text size="xl" fw={600}>
                {isSuccess ? numberFormat.format(response.data.totalWikis) : 'N/A'}
              </Text>
              <Text>Wikis</Text>
            </Stack>
          </Box>
          <Box className={classes.box}>
            <Stack gap={5}>
              <Text size="xl" fw={600}>
                {isSuccess ? numberFormat.format(response.data.totalPages) : 'N/A'}
              </Text>
              <Text>Checked pages</Text>
            </Stack>
          </Box>
          <Box className={classes.box}>
            <Stack gap={5}>
              <Text size="xl" fw={600}>
                {isSuccess ? numberFormat.format(response.data.totalDurationInMillis) : 'N/A'}
              </Text>
              <Text>Duration in millis</Text>
            </Stack>
          </Box>
          <Box className={classes.box}>
            <Stack gap={5}>
              <Text size="xl" fw={600}>
                {isSuccess ? numberFormat.format(response.data.totalLinks) : 'N/A'}
              </Text>
              <Text>Links</Text>
            </Stack>
          </Box>
          <Box className={classes.box}>
            <Stack gap={5}>
              <Text size="xl" fw={600}>
                {isSuccess ? numberFormat.format(response.data.totalIgnoredLinks) : 'N/A'}
              </Text>
              <Text>Ignored links</Text>
            </Stack>
          </Box>
          <Box className={classes.box}>
            <Stack gap={5}>
              <Text size="xl" fw={600}>
                {isSuccess ? numberFormat.format(response.data.totalWorkingLinks) : 'N/A'}
              </Text>
              <Text>Working links</Text>
            </Stack>
          </Box>
          <Box className={classes.box}>
            <Stack gap={5}>
              <Text size="xl" fw={600}>
                {isSuccess ? numberFormat.format(response.data.totalBrokenLinks) : 'N/A'}
              </Text>
              <Text>Broken links</Text>
            </Stack>
          </Box>
        </SimpleGrid>
      </Stack>
    </Container>
  );
}
