import { useTranslation } from 'react-i18next';
import {
  Anchor,
  Avatar,
  Group,
  Popover,
  Stack,
  Text,
  UnstyledButton,
  useDirection,
} from '@mantine/core';
import React, { useState } from 'react';
import {
  IconArrowLoopLeft2,
  IconArrowLoopRight2,
  IconCornerDownLeft,
  IconCornerDownRight,
} from '@tabler/icons-react';
import { Redirect } from '@/types/api/Check';
import classes from '@/pages/ResultPage/ResultPage.module.css';

interface ResultRedirectProps {
  redirects: Redirect[];
}

export function ResultRedirect({ redirects }: ResultRedirectProps) {
  const { t } = useTranslation();
  const { dir } = useDirection();
  const [opened, setOpened] = useState(false);

  const IconArrowLoop = dir === 'rtl' ? IconArrowLoopLeft2 : IconArrowLoopRight2;
  const IconCorner = dir === 'rtl' ? IconCornerDownLeft : IconCornerDownRight;

  const handleClickRedirectButton = (event: React.MouseEvent) => {
    event.stopPropagation();
    setOpened(!opened);
  };

  return (
    <Popover
      width={500}
      position="left-start"
      transitionProps={{ transition: 'fade-up' }}
      shadow="md"
      radius="md"
      opened={opened}
      onChange={setOpened}
    >
      <Popover.Target>
        <UnstyledButton
          className={classes.redirect}
          onClick={handleClickRedirectButton}
          data-opened={opened}
          aria-label={t('core:ui.result.redirectTitle', {
            redirectNumber: redirects.length,
          })}
          title={t('core:ui.result.redirectTitle', {
            redirectNumber: redirects.length,
          })}
        >
          <Text fw={600}>{redirects.length}</Text>
          <IconArrowLoop size="1rem" />
        </UnstyledButton>
      </Popover.Target>
      <Popover.Dropdown>
        <Stack gap="xs">
          {redirects.map((redirect, index) => (
            <Group key={index} gap="sm" wrap="nowrap" align="start">
              <Avatar variant="filled" color="blue" radius="xl" size="sm">
                {index + 1}
              </Avatar>
              <Stack gap={6}>
                <Anchor
                  href={redirect.requestUrl}
                  target="_blank"
                  size="xs"
                  ff="var(--mantine-alt-font-monospace)"
                  w="fit-content"
                  lh={1}
                  style={{ wordBreak: 'break-all' }}
                >
                  {redirect.requestUrl}
                </Anchor>
                <Group gap={6} wrap="nowrap">
                  <IconCorner size="1rem" stroke={1.5} style={{ minWidth: '1rem' }} />
                  <Anchor
                    href={redirect.location}
                    target="_blank"
                    size="xs"
                    ff="var(--mantine-alt-font-monospace)"
                    w="fit-content"
                    lh={1}
                    style={{ wordBreak: 'break-all' }}
                  >
                    {redirect.location}
                  </Anchor>
                </Group>
              </Stack>
            </Group>
          ))}
        </Stack>
      </Popover.Dropdown>
    </Popover>
  );
}
