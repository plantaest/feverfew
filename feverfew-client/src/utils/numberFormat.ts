import i18n from '@/i18n';

// eslint-disable-next-line import/no-mutable-exports
export let numberFormat = new Intl.NumberFormat(i18n.language);

i18n.on('languageChanged', () => {
  numberFormat = new Intl.NumberFormat(i18n.language);
});
