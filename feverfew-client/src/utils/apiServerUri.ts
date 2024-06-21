export const apiServerUri = (path?: string) => {
  const host =
    process.env.NODE_ENV === 'development'
      ? 'http://localhost:8001/api/v1/'
      : 'https://feverfew.toolforge.org/api/v1/';
  return path ? host + path : host;
};
