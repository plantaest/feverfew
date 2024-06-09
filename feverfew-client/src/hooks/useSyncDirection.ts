import { useDirection } from '@mantine/core';
import { useEffect } from 'react';

export function useSyncDirection() {
  const { dir } = useDirection();

  useEffect(() => {
    document.documentElement.setAttribute('dir', dir);
  }, [dir]);
}
