import {
  Anchor,
  Badge,
  CopyButton,
  Flex,
  Group,
  MantineColor,
  Stack,
  Text,
  UnstyledButton,
} from '@mantine/core';
import { IconArchive, IconBookmark, IconCopy } from '@tabler/icons-react';
import clsx from 'clsx';
import { useEffect, useRef } from 'react';
import classes from '@/pages/ResultPage/ResultPage.module.css';
import { EvaluationResult, RequestResultType } from '@/types/api/Check';
import { archiveHosts } from '@/utils/archiveHosts';
import { numberFormat } from '@/utils/numberFormat';
import { ResultRedirect } from '@/pages/ResultPage/components/ResultRedirect';
import { appState } from '@/states/appState';

const responseStatusColor = (status: string): MantineColor =>
  status.startsWith('2') ? 'teal' : status.startsWith('3') ? 'indigo' : 'pink';

interface ResultItemProps {
  result: EvaluationResult;
}

export function ResultItem({ result }: ResultItemProps) {
  const enabledReviewPanel = appState.review.enabled.get();
  const selectedResult = appState.review.selectedResult.get();
  const ref = useRef<HTMLDivElement>(null);

  useEffect(() => {
    appState.review.resultRefs.peek().set(result.index, ref);
  }, []);

  const handleClickResult = () => {
    if (enabledReviewPanel) {
      appState.review.selectedResult.set(result);
      ref.current?.scrollIntoView({
        block: 'start',
        behavior: 'smooth',
      });
      setTimeout(() => ref.current?.focus(), 125);
    }
  };

  return (
    <UnstyledButton
      component="div"
      tabIndex={0}
      className={classes.result}
      onClick={handleClickResult}
      data-selected={result.index === selectedResult?.index}
      ref={ref}
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
                [classes.ignoredBg]: result.requestResult.type === RequestResultType.IGNORED,
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
              <Text fw={600}>{Math.round(result.classificationResult.probability * 100)}%</Text>
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
                    color={responseStatusColor(String(result.requestResult.responseStatus))}
                  >
                    {result.requestResult.responseStatus >= 100
                      ? result.requestResult.responseStatus
                      : 'ERR'}
                  </Badge>
                  <Badge tt="none" radius="sm" variant="light" color="grape">
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
                      color={copied ? 'var(--mantine-color-teal-5)' : 'var(--mantine-color-blue-5)'}
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
                    color={copied ? 'var(--mantine-color-teal-5)' : 'var(--mantine-color-blue-5)'}
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
  );
}
