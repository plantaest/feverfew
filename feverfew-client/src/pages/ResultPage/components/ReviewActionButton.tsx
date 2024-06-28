import { ActionIcon, Affix } from '@mantine/core';
import { IconEyeglass } from '@tabler/icons-react';
import { useTranslation } from 'react-i18next';
import { appState } from '@/states/appState';

export function ReviewActionButton() {
  const { t } = useTranslation();
  const enabledReviewPanel = appState.review.enabled.get();

  const handleClickReviewButton = () => appState.review.enabled.set(true);

  return (
    !enabledReviewPanel && (
      <Affix
        visibleFrom="lg"
        position={{
          bottom: 15,
          right: 15,
        }}
      >
        <ActionIcon
          variant="filled"
          size="xl"
          radius="xl"
          title={t('core:ui.review.title')}
          aria-label={t('core:ui.review.title')}
          onClick={handleClickReviewButton}
        >
          <IconEyeglass
            style={{
              width: '60%',
              height: '60%',
            }}
            stroke={1.5}
          />
        </ActionIcon>
      </Affix>
    )
  );
}
