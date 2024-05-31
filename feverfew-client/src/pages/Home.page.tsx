import { Box, Button, Container, Grid, Select, Stack, TextInput, Title } from '@mantine/core';
import { useFocusTrap } from '@mantine/hooks';
import { Header } from '@/components/Header/Header';
import { Footer } from '@/components/Footer/Footer';

const wikiIds = ['bnwiki', 'enwiki', 'eswiki', 'viwiki'];

export function HomePage() {
  const focusTrapRef = useFocusTrap();

  return (
    <>
      <Stack h="100vh" justify="space-between">
        <Box>
          <Header />
          <Container size="xl" mt={75}>
            <Stack>
              <Title
                fz={{
                  base: 50,
                  sm: 60,
                  md: 80,
                }}
                fw={600}
                ta="center"
                ff="var(--mantine-alt-font-family)"
              >
                Feverfew
              </Title>
              <Grid ref={focusTrapRef} mt={60} gutter="sm">
                <Grid.Col
                  span={{
                    base: 12,
                    md: 3,
                    lg: 2.5,
                  }}
                >
                  <Select
                    variant="filled"
                    size="lg"
                    radius="md"
                    placeholder="Select wiki"
                    data={wikiIds}
                    styles={{ dropdown: { borderRadius: 'var(--mantine-radius-md)' } }}
                  />
                </Grid.Col>
                <Grid.Col
                  span={{
                    base: 12,
                    md: 7,
                    lg: 8,
                  }}
                >
                  <TextInput
                    size="lg"
                    radius="md"
                    placeholder="Enter article title"
                    data-autofocus
                  />
                </Grid.Col>
                <Grid.Col
                  span={{
                    base: 12,
                    md: 2,
                    lg: 1.5,
                  }}
                >
                  <Button variant="filled" size="lg" radius="md" w="100%">
                    Check
                  </Button>
                </Grid.Col>
              </Grid>
            </Stack>
          </Container>
        </Box>
        <Footer />
      </Stack>
    </>
  );
}
