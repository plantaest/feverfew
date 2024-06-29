import i18n from 'i18next';
import Backend from 'i18next-http-backend';
import { initReactI18next } from 'react-i18next';
import LanguageDetector from 'i18next-browser-languagedetector';
import ICU from 'i18next-icu';
import { serverUri } from '@/utils/serverUri';
import { appConfig } from '@/config/appConfig';

i18n
  .use(ICU)
  .use(Backend)
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    debug: appConfig.DEBUG,
    load: 'languageOnly',
    detection: {
      order: ['localStorage', 'navigator'],
    },
    fallbackLng: 'en',
    supportedLngs: ['en', 'es', 'vi'],
    interpolation: {
      escapeValue: false,
    },
    ns: ['core'],
    defaultNS: 'core',
    backend: {
      loadPath: serverUri('locales/{{lng}}/{{ns}}.json'),
    },
    react: {
      transSupportBasicHtmlNodes: true,
    },
  })
  .then();

export default i18n;
