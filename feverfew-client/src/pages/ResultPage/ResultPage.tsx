import {
  ActionIcon,
  Anchor,
  Badge,
  Card,
  Container,
  CopyButton,
  Flex,
  Group,
  MantineColor,
  Popover,
  SimpleGrid,
  Stack,
  Text,
  TextInput,
  UnstyledButton,
} from '@mantine/core';
import { useSearchParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import clsx from 'clsx';
import {
  IconArchive,
  IconBookmark,
  IconCopy,
  IconExternalLink,
  IconLink,
  IconShare,
} from '@tabler/icons-react';
import { useDocumentTitle } from '@mantine/hooks';
import dayjs from 'dayjs';
import { useTranslation } from 'react-i18next';
import { CreateCheckResponse, RequestResultType } from '@/types/api/Check';
import classes from './ResultPage.module.css';
import { useCreateCheck } from '@/hooks/useCreateCheck';
import { numberFormat } from '@/utils/numberFormat';
import { MwHelper } from '@/utils/MwHelper';
import { archiveHosts } from '@/utils/archiveHosts';
import { ResultRedirect } from './ResultRedirect';
import { ResultError } from './ResultError';
import { ResultSkeleton } from './ResultSkeleton';
import { ResultList } from './ResultList';

const responseStatusColor = (status: string): MantineColor =>
  status.startsWith('2') ? 'teal' : status.startsWith('3') ? 'indigo' : 'pink';

export function ResultPage() {
  const { t } = useTranslation();
  const [searchParams] = useSearchParams();

  const wikiId = searchParams.get('wiki')?.trim() || null;
  const pageTitle = searchParams.get('page')?.replaceAll('_', ' ').trim() || null;

  const shouldCheck = wikiId && pageTitle;

  const [response, setResponse] = useState<CreateCheckResponse | null>(null);

  const createCheckApi = useCreateCheck();

  useDocumentTitle(
    shouldCheck ? `[${wikiId}] ${response ? response.pageTitle : pageTitle} – Feverfew` : 'Feverfew'
  );

  useEffect(() => {
    if (shouldCheck) {
      createCheckApi.mutate(
        {
          wikiId: wikiId,
          pageTitle: pageTitle,
          pageRevisionId: null,
        },
        {
          onSuccess: (r) => setResponse(r.data),
        }
      );
    }
  }, [shouldCheck]);

  if (!shouldCheck) {
    return <ResultList />;
  }

  return (
    <Container size="xl">
      {createCheckApi.isSuccess && response ? (
        <Stack my="md" gap="sm">
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
                <Text fw={600} ff="var(--mantine-alt-font-monospace)" c="blue">
                  {response.wikiId}
                </Text>
              </Group>

              <Group justify="space-between" gap="xs" mih={28}>
                <Text fz="sm" c="dimmed">
                  {t('core:ui.result.note', {
                    time: dayjs(response.createdAt).format('HH:mm:ss'),
                    date: dayjs(response.createdAt).format('YYYY-MM-DD'),
                    duration: numberFormat.format(
                      Number((response.durationInMillis / 1000).toFixed(2))
                    ),
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
                    aria-label={t('core:ui.result.wikiPageLinkTitle')}
                    title={t('core:ui.result.wikiPageLinkTitle')}
                    component="a"
                    href={MwHelper.createPageUri(response.wikiServerName, response.pageTitle)}
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
                  <Popover width={300} position="bottom-end" shadow="md" radius="md">
                    <Popover.Target>
                      <ActionIcon
                        variant="subtle"
                        aria-label={t('core:ui.result.shareTitle')}
                        title={t('core:ui.result.shareTitle')}
                      >
                        <IconShare
                          style={{
                            width: '70%',
                            height: '70%',
                          }}
                          stroke={1.5}
                        />
                      </ActionIcon>
                    </Popover.Target>
                    <Popover.Dropdown>
                      <TextInput
                        styles={{
                          input: {
                            fontSize: 'var(--mantine-font-size-xs)',
                            fontFamily: 'var(--mantine-alt-font-monospace)',
                          },
                        }}
                        label={t('core:ui.result.checkLinkTitle')}
                        defaultValue={`${window.location.protocol}//${window.location.host}/check/archive/${response.id}`}
                        rightSection={
                          <CopyButton
                            value={`${window.location.protocol}//${window.location.host}/check/archive/${response.id}`}
                          >
                            {({ copied, copy }) => (
                              <IconCopy
                                style={{
                                  minWidth: '0.85rem',
                                  cursor: 'pointer',
                                }}
                                size="0.85rem"
                                color={
                                  copied
                                    ? 'var(--mantine-color-teal-5)'
                                    : 'var(--mantine-color-blue-5)'
                                }
                                onClick={copy}
                              />
                            )}
                          </CopyButton>
                        }
                      />
                    </Popover.Dropdown>
                  </Popover>
                </Group>
              </Group>
            </Flex>
          </Card>

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

          <Stack gap="xs">
            {response.results.map((result) => (
              <UnstyledButton
                component="div"
                tabIndex={0}
                key={result.link.id}
                className={classes.result}
              >
                <Group wrap="nowrap" gap="xs" align="stretch" justify="space-between">
                  <Group wrap="nowrap" gap="xs" align="stretch" miw={0}>
                    <Flex className={classes.index}>
                      <Text ff="var(--mantine-alt-font-monospace)" fz="lg">
                        {result.index}
                      </Text>
                      {result.link.refIndex && (
                        <Text
                          size="sm"
                          fw={600}
                          c={result.link.refIndex % 2 === 0 ? 'orange' : 'violet'}
                          display="inline-flex"
                          style={{
                            alignItems: 'center',
                            gap: '0.25rem',
                          }}
                        >
                          <IconBookmark size="0.75rem" stroke={3} /> {result.link.refIndex}
                        </Text>
                      )}
                    </Flex>

                    <Flex
                      className={clsx(
                        classes.percent,
                        {
                          [classes.ignoredBg]:
                            result.requestResult.type === RequestResultType.IGNORED,
                        },
                        {
                          [classes.workingBg]:
                            result.requestResult.type !== RequestResultType.IGNORED &&
                            result.classificationResult.label === 0,
                        },
                        {
                          [classes.brokenBg]:
                            result.requestResult.type !== RequestResultType.IGNORED &&
                            result.classificationResult.label === 1,
                        }
                      )}
                    >
                      {result.requestResult.type === RequestResultType.IGNORED &&
                        archiveHosts.includes(result.link.host) && <IconArchive stroke={1.5} />}
                      {result.requestResult.type !== RequestResultType.IGNORED && (
                        <Text fw={600}>
                          {Math.round(result.classificationResult.probability * 100)}%
                        </Text>
                      )}
                    </Flex>

                    <Stack gap={5} miw={0}>
                      <Group gap={8}>
                        <Badge tt="none" radius="sm" variant="light">
                          {result.link.host}
                        </Badge>
                        {result.requestResult.type !== RequestResultType.IGNORED && (
                          <>
                            <Badge
                              tt="none"
                              radius="sm"
                              variant="filled"
                              color={responseStatusColor(
                                String(result.requestResult.responseStatus)
                              )}
                            >
                              {result.requestResult.responseStatus >= 100
                                ? result.requestResult.responseStatus
                                : 'ERR'}
                            </Badge>
                            <Badge tt="none" radius="sm" variant="light">
                              {numberFormat.format(result.requestResult.requestDuration)} ms
                            </Badge>
                            <Badge tt="none" radius="sm" variant="light">
                              {numberFormat.format(result.requestResult.contentLength)} byte(s)
                            </Badge>
                          </>
                        )}
                        {result.link.refName && result.link.refIndex && (
                          <Badge
                            tt="none"
                            radius="sm"
                            variant="filled"
                            color={result.link.refIndex % 2 === 0 ? 'orange' : 'violet'}
                            leftSection={<IconBookmark size="0.75rem" stroke={3} />}
                          >
                            {result.link.refName}
                          </Badge>
                        )}
                      </Group>

                      {result.link.text && (
                        <Flex display="inline-flex" align="center">
                          <Text fw={600} me={6} lh={1.2} style={{ wordBreak: 'break-word' }}>
                            {result.link.text}
                          </Text>
                          <CopyButton value={result.link.text}>
                            {({ copied, copy }) => (
                              <IconCopy
                                style={{ minWidth: '0.85rem' }}
                                size="0.85rem"
                                color={
                                  copied
                                    ? 'var(--mantine-color-teal-5)'
                                    : 'var(--mantine-color-blue-5)'
                                }
                                onClick={copy}
                              />
                            )}
                          </CopyButton>
                        </Flex>
                      )}

                      <Flex display="inline-flex" align="center">
                        <Anchor
                          size="xs"
                          ff="var(--mantine-alt-font-monospace)"
                          w="fit-content"
                          me={6}
                          href={result.link.href}
                          target="_blank"
                          style={{ wordBreak: 'break-all' }}
                          inline
                        >
                          {result.link.href}
                        </Anchor>
                        <CopyButton value={result.link.href}>
                          {({ copied, copy }) => (
                            <IconCopy
                              style={{ minWidth: '0.85rem' }}
                              size="0.85rem"
                              color={
                                copied
                                  ? 'var(--mantine-color-teal-5)'
                                  : 'var(--mantine-color-blue-5)'
                              }
                              onClick={copy}
                            />
                          )}
                        </CopyButton>
                      </Flex>
                    </Stack>
                  </Group>

                  {result.requestResult.redirects.length > 0 && (
                    <ResultRedirect redirects={result.requestResult.redirects} />
                  )}
                </Group>
              </UnstyledButton>
            ))}
          </Stack>
        </Stack>
      ) : createCheckApi.isError ? (
        <ResultError />
      ) : (
        <ResultSkeleton />
      )}
    </Container>
  );
}
