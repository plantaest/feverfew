import { ActionIcon, Container, Group, Menu } from '@mantine/core';
import { IconGridDots } from '@tabler/icons-react';
import { Link, useLocation } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import classes from './Header.module.css';
import { ColorSchemeToggle } from '@/components/ColorSchemeToggle/ColorSchemeToggle';
import { DirectionToggle } from '@/components/DirectionToggle/DirectionToggle';
import { LanguageToggle } from '@/components/LanguageToggle/LanguageToggle';

const links = [
  {
    link: '/',
    label: 'core:ui.header.home',
  },
  {
    link: '/check',
    label: 'core:ui.header.result',
  },
  {
    link: '/stats',
    label: 'core:ui.header.stats',
  },
];

export function Header() {
  const location = useLocation();
  const { t } = useTranslation();

  const items = links.map((link) => (
    <Link
      key={link.label}
      to={link.link}
      className={classes.link}
      data-active={
        link.link === '/'
          ? location.pathname === link.link
          : location.pathname.startsWith(link.link)
      }
    >
      {t(link.label)}
    </Link>
  ));

  return (
    <header className={classes.header}>
      <Container size="xl" className={classes.inner}>
        <Group gap="xs" visibleFrom="sm">
          {items}
        </Group>
        <Menu width={200} shadow="md" position="bottom-start">
          <Menu.Target>
            <ActionIcon hiddenFrom="sm" variant="light" aria-label="Menu" color="gray">
              <IconGridDots
                style={{
                  width: '70%',
                  height: '70%',
                }}
                stroke={1.5}
              />
            </ActionIcon>
          </Menu.Target>
          <Menu.Dropdown>
            {links.map((link) => (
              <Menu.Item
                key={link.label}
                component={Link}
                to={link.link}
                c={location.pathname === link.link ? 'blue' : 'default'}
              >
                {t(link.label)}
              </Menu.Item>
            ))}
          </Menu.Dropdown>
        </Menu>
        <Group gap="xs">
          <ColorSchemeToggle />
          <DirectionToggle />
          <LanguageToggle />
        </Group>
      </Container>
    </header>
  );
}
