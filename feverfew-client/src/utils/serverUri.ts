export const serverUri = (path?: string) => {
  const host =
    process.env.NODE_ENV === 'development'
      ? 'http://localhost:8020/'
      : 'https://feverfew.toolforge.org/';
  return path ? host + path : host;
};
