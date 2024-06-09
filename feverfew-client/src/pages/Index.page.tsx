import { Box, Stack } from '@mantine/core';
import { Outlet } from 'react-router-dom';
import { Header } from '@/components/Header/Header';
import { Footer } from '@/components/Footer/Footer';
import { useSyncDirection } from '@/hooks/useSyncDirection';

export function IndexPage() {
  useSyncDirection();

  return (
    <Stack h="100vh" justify="space-between">
      <Box>
        <Header />
        <Outlet />
      </Box>
      <Footer />
    </Stack>
  );
}
