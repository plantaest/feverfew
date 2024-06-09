import { ActionIcon, Menu } from '@mantine/core';
import { IconWorld } from '@tabler/icons-react';
import { useTranslation } from 'react-i18next';

const langs = ['en', 'vi'];

export function LanguageToggle() {
  const { t, i18n } = useTranslation();

  const handleChangeLanguage = async (lang: string) => {
    await i18n.changeLanguage(lang);
  };

  return (
    <Menu width={200} shadow="md" position="bottom-end">
      <Menu.Target>
        <ActionIcon
          variant="light"
          aria-label={t('core:ui.header.changeLanguage')}
          title={t('core:ui.header.changeLanguage')}
          color="gray"
        >
          <IconWorld
            style={{
              width: '70%',
              height: '70%',
            }}
            stroke={1.5}
          />
        </ActionIcon>
      </Menu.Target>
      <Menu.Dropdown>
        {langs.map((lang) => (
          <Menu.Item
            key={lang}
            c={lang === i18n.language ? 'blue' : 'default'}
            onClick={() => handleChangeLanguage(lang)}
          >
            {lang}
          </Menu.Item>
        ))}
      </Menu.Dropdown>
    </Menu>
  );
}
