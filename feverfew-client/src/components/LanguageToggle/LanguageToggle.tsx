import { ActionIcon, Menu } from '@mantine/core';
import { IconWorld } from '@tabler/icons-react';
import { useTranslation } from 'react-i18next';

const langs = [
  {
    value: 'en',
    label: 'English',
  },
  {
    value: 'es',
    label: 'Español',
  },
  {
    value: 'vi',
    label: 'Tiếng Việt',
  },
];

export function LanguageToggle() {
  const { t, i18n } = useTranslation();

  const handleChangeLanguage = async (lang: string) => {
    await i18n.changeLanguage(lang);
  };

  return (
    <Menu width={200} shadow="md" position="bottom-end" radius="md">
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
            key={lang.value}
            c={lang.value === i18n.language ? 'blue' : 'default'}
            onClick={() => handleChangeLanguage(lang.value)}
          >
            {lang.label}
          </Menu.Item>
        ))}
      </Menu.Dropdown>
    </Menu>
  );
}
