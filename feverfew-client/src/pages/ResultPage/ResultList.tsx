import {
  Anchor,
  Badge,
  Container,
  Flex,
  Group,
  Pagination,
  Skeleton,
  Stack,
  Text,
  Title,
} from '@mantine/core';
import { Link } from 'react-router-dom';
import dayjs from 'dayjs';
import React, { useState } from 'react';
import { IconCalendarMonth, IconUser } from '@tabler/icons-react';
import { useTranslation } from 'react-i18next';
import classes from './ResultList.module.css';
import { numberFormat } from '@/utils/numberFormat';
import { useGetListCheck } from '@/hooks/useGetListCheck';
import { ResultError } from '@/pages/ResultPage/ResultError';
import { ResultListItemsSkeleton } from '@/pages/ResultPage/ResultListItemsSkeleton';

const PAGE_SIZE = 5;

export function ResultList() {
  const { t } = useTranslation();
  const [activePage, setPage] = useState(1);

  const { data: response, isSuccess, isLoading, isError } = useGetListCheck(activePage, PAGE_SIZE);

  const dates = new Set<string>();

  return (
    <Container size="xl">
      <Stack my="md" gap="lg">
        <Title order={2} fw={400} ff="var(--mantine-alt-font-family)">
          {t('core:ui.result.list.title')}
        </Title>

        {isSuccess ? (
          <Stack gap="xs">
            {response.data.items.map((check, index) => {
              const date = dayjs(check.createdAt).format('YYYY-MM-DD');
              let showDate;

              if (dates.has(date)) {
                showDate = false;
              } else {
                showDate = true;
                dates.add(date);
              }

              return (
                <React.Fragment key={index}>
                  {showDate && (
                    <Text
                      component="div"
                      ff="var(--mantine-alt-font-monospace)"
                      fz="sm"
                      fw={600}
                      c="dimmed"
                      mih={22}
                    >
                      <Group gap={6}>
                        <IconCalendarMonth size="1.25rem" stroke={1.5} />
                        {dayjs(check.createdAt).format('YYYY-MM-DD')}
                      </Group>
                    </Text>
                  )}
                  <Anchor
                    className={classes.check}
                    underline="never"
                    c="inherit"
                    component={Link}
                    to={`archive/${check.id}`}
                  >
                    <Stack gap={7}>
                      <Group justify="space-between" gap={8}>
                        <Group gap="xs">
                          <Text fw={600} ff="var(--mantine-alt-font-monospace)" c="cyan" size="xs">
                            {dayjs(check.createdAt).format('HH:mm:ss')}
                          </Text>
                          <Text fw={600} ff="var(--mantine-alt-font-monospace)" c="blue" size="xs">
                            {check.wikiId}
                          </Text>
                          <Text
                            component="div"
                            fw={600}
                            ff="var(--mantine-alt-font-monospace)"
                            c="grape"
                            size="xs"
                          >
                            <Group gap={4}>
                              <IconUser size="0.85rem" />
                              {check.createdBy}
                            </Group>
                          </Text>
                        </Group>
                        <Text fw={600} ff="var(--mantine-alt-font-monospace)" c="orange" size="xs">
                          {numberFormat.format(Number((check.durationInMillis / 1000).toFixed(2)))}{' '}
                          s
                        </Text>
                      </Group>

                      <Group justify="space-between" wrap="nowrap" gap="xs">
                        <Text
                          fz="lg"
                          style={{
                            whiteSpace: 'nowrap',
                            textOverflow: 'ellipsis',
                            overflow: 'hidden',
                          }}
                        >
                          {check.pageTitle}
                        </Text>
                        <Group gap={6} wrap="nowrap">
                          <Badge
                            ff="var(--mantine-alt-font-monospace)"
                            radius="sm"
                            h={26}
                            color="blue"
                          >
                            {check.totalLinks}
                          </Badge>
                          <Badge
                            ff="var(--mantine-alt-font-monospace)"
                            radius="sm"
                            h={26}
                            className={classes.ignoredBg}
                          >
                            {check.totalIgnoredLinks}
                          </Badge>
                          <Badge
                            ff="var(--mantine-alt-font-monospace)"
                            radius="sm"
                            h={26}
                            color="teal"
                          >
                            {check.totalWorkingLinks}
                          </Badge>
                          <Badge
                            ff="var(--mantine-alt-font-monospace)"
                            radius="sm"
                            h={26}
                            color="pink"
                          >
                            {check.totalBrokenLinks}
                          </Badge>
                        </Group>
                      </Group>
                    </Stack>
                  </Anchor>
                </React.Fragment>
              );
            })}
          </Stack>
        ) : isError ? (
          <ResultError />
        ) : (
          <ResultListItemsSkeleton />
        )}

        <Flex justify="center" mt="md">
          {isSuccess ? (
            <Pagination
              value={activePage}
              onChange={setPage}
              total={response.data.totalPages}
              radius="md"
            />
          ) : isLoading ? (
            <Skeleton height={32} width={200} radius="md" />
          ) : null}
        </Flex>
      </Stack>
    </Container>
  );
}
