import { Anchor, Box, Card, Drawer, Flex, Group, Stack, Text } from '@mantine/core';
import { useTranslation } from 'react-i18next';
import { encode } from 'html-entities';
import { useEffect, useRef } from 'react';
import { appState } from '@/states/appState';
import classes from './ReviewPanel.module.css';
import { useGetRevisionWikitext } from '@/hooks/useGetRevisionWikitext';

const coloredWikitext = (wikitext: string, substring: string) =>
  encode(wikitext).replaceAll(
    substring.replaceAll('&', '&amp;'),
    `<span class=${classes.selectedLink}>${substring}</span>`
  );

interface ReviewPanelProps {
  wikiId: string;
  revisionId: number;
}

export function ReviewPanel({ wikiId, revisionId }: ReviewPanelProps) {
  const { t } = useTranslation();
  const enabledReviewPanel = appState.review.enabled.get();
  const selectedResult = appState.review.selectedResult.get();
  const wikitextCardRef = useRef<HTMLDivElement>(null);

  const { data: response, isSuccess, isError } = useGetRevisionWikitext(wikiId, revisionId);

  useEffect(() => {
    if (selectedResult?.index !== 0) {
      const firstSelectedLink = wikitextCardRef.current?.querySelector(`.${classes.selectedLink}`);

      if (firstSelectedLink) {
        setTimeout(() => firstSelectedLink.scrollIntoView({ behavior: 'smooth' }), 425);
      }
    }
  }, [selectedResult?.index ?? 0]);

  const handleCloseDrawer = () => {
    appState.review.enabled.set(false);
    appState.review.selectedResult.set(null);
  };

  return (
    <Drawer.Root
      opened={enabledReviewPanel}
      onClose={handleCloseDrawer}
      position="bottom"
      lockScroll={false}
      padding="xs"
      size={550}
    >
      <Drawer.Content className={classes.content}>
        <Drawer.Body h="100%">
          {selectedResult ? (
            <Stack gap="xs" h="100%">
              <Group wrap="nowrap" gap="xs">
                <Group wrap="nowrap" gap="xs" miw={0}>
                  <Text
                    ff="var(--mantine-alt-font-monospace)"
                    c="dimmed"
                    ps={8}
                    pe={6}
                    ta="center"
                    size="lg"
                  >
                    {selectedResult.index}
                  </Text>
                  <Flex
                    direction="column"
                    style={{
                      whiteSpace: 'nowrap',
                      overflow: 'hidden',
                    }}
                  >
                    {selectedResult.link.text && (
                      <Text
                        size="sm"
                        fw={600}
                        style={{
                          textOverflow: 'ellipsis',
                          overflow: 'hidden',
                        }}
                      >
                        {selectedResult.link.text}
                      </Text>
                    )}
                    <Anchor
                      size="xs"
                      ff="var(--mantine-alt-font-monospace)"
                      href={selectedResult.link.href}
                      target="_blank"
                      style={{
                        textOverflow: 'ellipsis',
                        overflow: 'hidden',
                      }}
                    >
                      {selectedResult.link.href}
                    </Anchor>
                  </Flex>
                </Group>
                <Drawer.CloseButton />
              </Group>

              <Flex gap="xs" h="100%" style={{ overflow: 'hidden' }}>
                <Card flex={4} withBorder padding={0} radius="md" h="100%">
                  <iframe
                    src={selectedResult.link.href}
                    title={selectedResult.link.href}
                    height="100%"
                    style={{
                      border: 'none',
                      overflowY: 'auto',
                      overscrollBehavior: 'contain',
                    }}
                  />
                </Card>
                <Box className={classes.wikitextBox}>
                  <Card padding="xs" className={classes.wikitextCard} ref={wikitextCardRef}>
                    {isSuccess ? (
                      <Box
                        dangerouslySetInnerHTML={{
                          __html: coloredWikitext(response.data.wikitext, selectedResult.link.href),
                        }}
                      />
                    ) : isError ? (
                      'ERR'
                    ) : (
                      '...'
                    )}
                  </Card>
                </Box>
              </Flex>
            </Stack>
          ) : (
            <Stack h="100%">
              <Drawer.CloseButton />
              <Flex justify="center" align="center" h="100%">
                <Text c="dimmed" size="xl">
                  {t('core:ui.review.select')}
                </Text>
              </Flex>
            </Stack>
          )}
        </Drawer.Body>
      </Drawer.Content>
    </Drawer.Root>
  );
}
