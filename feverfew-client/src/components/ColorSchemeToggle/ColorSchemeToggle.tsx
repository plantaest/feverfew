import { ActionIcon, useMantineColorScheme } from '@mantine/core';
import { IconDeviceDesktop, IconMoon, IconSun } from '@tabler/icons-react';
import { useState } from 'react';

const colorSchemeValues = ['auto', 'light', 'dark'] as const;

export function ColorSchemeToggle() {
  const { colorScheme, setColorScheme } = useMantineColorScheme();
  const initialColorSchemeValueIndex = colorSchemeValues.findIndex(
    (value) => value === colorScheme
  );
  const [colorSchemeValueIndex, setColorSchemeValueIndex] = useState(initialColorSchemeValueIndex);

  const handleToggleColorSchemeButton = () => {
    const properColorSchemeValueIndex = colorSchemeValueIndex >= 2 ? 0 : colorSchemeValueIndex + 1;
    setColorSchemeValueIndex(properColorSchemeValueIndex);
    setColorScheme(colorSchemeValues[properColorSchemeValueIndex]);
  };

  const Icon =
    colorScheme === 'auto' ? IconDeviceDesktop : colorScheme === 'dark' ? IconMoon : IconSun;

  return (
    <ActionIcon
      variant="light"
      aria-label="Color scheme toggle"
      color="gray"
      onClick={handleToggleColorSchemeButton}
    >
      <Icon
        style={{
          width: '70%',
          height: '70%',
        }}
        stroke={1.5}
      />
    </ActionIcon>
  );
}
