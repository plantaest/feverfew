export const appConfig = Object.freeze({
  DEBUG: process.env.NODE_ENV === 'development',
  VERSION: APP_VERSION,
  API_USER_AGENT: `Feverfew/${APP_VERSION}`,
  RESULT_LIST_SIZE: 7,
});
