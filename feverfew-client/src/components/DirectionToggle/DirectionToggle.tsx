import { IconTextDirectionLtr, IconTextDirectionRtl } from '@tabler/icons-react';
import { ActionIcon, useDirection } from '@mantine/core';
import { useTranslation } from 'react-i18next';
import { appState } from '@/states/appState';

export function DirectionToggle() {
  const { t } = useTranslation();
  const { dir, setDirection } = useDirection();

  const IconDirection = dir === 'rtl' ? IconTextDirectionLtr : IconTextDirectionRtl;

  const handleToggleDirectionButton = () => {
    const nextDir = dir === 'rtl' ? 'ltr' : 'rtl';
    setDirection(nextDir);
    appState.local.dir.set(nextDir);
  };

  return (
    <ActionIcon
      variant="light"
      aria-label={t('core:ui.header.changeDirection')}
      title={t('core:ui.header.changeDirection')}
      color="gray"
      onClick={handleToggleDirectionButton}
    >
      <IconDirection
        style={{
          width: '70%',
          height: '70%',
        }}
        stroke={1.5}
      />
    </ActionIcon>
  );
}
