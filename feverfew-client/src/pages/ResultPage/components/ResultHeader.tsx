import { ActionIcon, Card, Flex, Group, Text } from '@mantine/core';
import dayjs from 'dayjs';
import { IconExternalLink, IconLink } from '@tabler/icons-react';
import { useTranslation } from 'react-i18next';
import { numberFormat } from '@/utils/numberFormat';
import { MwHelper } from '@/utils/MwHelper';
import { CreateCheckResponse, GetOneCheckResponse } from '@/types/api/Check';

interface ResultHeaderProps {
  response: GetOneCheckResponse | CreateCheckResponse;
}

export function ResultHeader({ response }: ResultHeaderProps) {
  const { t } = useTranslation();

  return (
    <Card radius="md" px="lg" py="sm" withBorder>
      <Flex direction="column">
        <Group justify="space-between" gap="xs" wrap="nowrap">
          <Text
            fz="xl"
            fw={600}
            style={{
              whiteSpace: 'nowrap',
              textOverflow: 'ellipsis',
              overflow: 'hidden',
            }}
          >
            {response.pageTitle}
          </Text>
          <Group align="baseline" gap="xs" wrap="nowrap">
            <Text fz="sm" fw={600} ff="var(--mantine-alt-font-monospace)" c="dimmed">
              {response.pageRevisionId}
            </Text>
            <Text fw={600} ff="var(--mantine-alt-font-monospace)" c="blue">
              {response.wikiId}
            </Text>
          </Group>
        </Group>

        <Group justify="space-between" gap="xs" mih={28}>
          <Text fz="sm" c="dimmed">
            {t('core:ui.result.note', {
              time: dayjs(response.createdAt).format('HH:mm:ss'),
              date: dayjs(response.createdAt).format('YYYY-MM-DD'),
              duration: numberFormat.format(Number((response.durationInMillis / 1000).toFixed(2))),
            })}
          </Text>
          <Group gap={4} visibleFrom="xs">
            <ActionIcon
              variant="subtle"
              aria-label={t('core:ui.result.checkLinkTitle')}
              title={t('core:ui.result.checkLinkTitle')}
              component="a"
              href={`/check/archive/${response.id}`}
              target="_blank"
            >
              <IconLink
                style={{
                  width: '70%',
                  height: '70%',
                }}
                stroke={1.5}
              />
            </ActionIcon>
            <ActionIcon
              variant="subtle"
              aria-label={t('core:ui.result.revisionLinkTitle')}
              title={t('core:ui.result.revisionLinkTitle')}
              component="a"
              href={MwHelper.createRevisionUri(
                response.wikiServerName,
                response.pageTitle,
                response.pageRevisionId
              )}
              target="_blank"
            >
              <IconExternalLink
                style={{
                  width: '70%',
                  height: '70%',
                }}
                stroke={1.5}
              />
            </ActionIcon>
          </Group>
        </Group>
      </Flex>
    </Card>
  );
}
