import { useState } from 'react';
import { ActionIcon, Container, Group, Menu } from '@mantine/core';
import { IconGridDots } from '@tabler/icons-react';
import classes from './Header.module.css';
import { ColorSchemeToggle } from '@/components/ColorSchemeToggle/ColorSchemeToggle';

const links = [
  {
    link: '/',
    label: 'Home',
  },
  {
    link: '/result',
    label: 'Result',
  },
  {
    link: '/stats',
    label: 'Stats',
  },
  {
    link: '/user',
    label: 'User',
  },
];

export function Header() {
  const [active, setActive] = useState(links[0].link);

  const items = links.map((link) => (
    <a
      key={link.label}
      href={link.link}
      className={classes.link}
      data-active={active === link.link}
      onClick={(event) => {
        event.preventDefault();
        setActive(link.link);
      }}
    >
      {link.label}
    </a>
  ));

  return (
    <header className={classes.header}>
      <Container size="xl" className={classes.inner}>
        <Group gap={6} visibleFrom="sm">
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
              <Menu.Item key={link.label}>{link.label}</Menu.Item>
            ))}
          </Menu.Dropdown>
        </Menu>
        <Group gap="xs">
          <ColorSchemeToggle />
        </Group>
      </Container>
    </header>
  );
}
