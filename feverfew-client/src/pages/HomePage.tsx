import { Button, Container, Grid, Select, Stack, TextInput, Title } from '@mantine/core';
import { useDocumentTitle, useFocusTrap } from '@mantine/hooks';
import { useTranslation } from 'react-i18next';
import { z } from 'zod';
import { useForm, zodResolver } from '@mantine/form';
import { useNavigate } from 'react-router-dom';
import { errorMessage } from '@/utils/errorMessage';
import { appState } from '@/states/appState';
import { wikiIds } from '@/utils/wikiIds';

const formSchema = (t: (key: string) => string) =>
  z.object({
    wikiId: z
      .string({ invalid_type_error: t(errorMessage.notEmpty) })
      .min(1, t(errorMessage.notEmpty)),
    pageTitle: z.string().trim().min(1, t(errorMessage.notEmpty)),
  });

type FormValues = z.infer<ReturnType<typeof formSchema>>;

export function HomePage() {
  useDocumentTitle('Feverfew');

  const { t } = useTranslation();
  const navigate = useNavigate();
  const focusTrapRef = useFocusTrap();

  const initialFormValues: FormValues = {
    wikiId: appState.local.selectedWikiId.get() ?? '',
    pageTitle: '',
  };

  const form = useForm({
    initialValues: initialFormValues,
    validate: zodResolver(formSchema(t)),
  });

  form.watch('wikiId', ({ value }) => {
    appState.local.selectedWikiId.set(value);
  });

  const handleFormSubmit = form.onSubmit((formValues) => {
    navigate(
      `/check?wiki=${formValues.wikiId}&page=${formValues.pageTitle.trim().replaceAll(' ', '_')}`
    );
  });

  return (
    <Container size="xl" mt={75} mb={50}>
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
        <form onSubmit={handleFormSubmit}>
          <Grid ref={focusTrapRef} mt={60} gutter="sm">
            <Grid.Col
              span={{
                base: 12,
                md: 3,
                lg: 2.5,
              }}
            >
              <Select
                size="lg"
                radius="md"
                placeholder={t('core:ui.home.selectWiki')}
                data={wikiIds}
                styles={{ dropdown: { borderRadius: 'var(--mantine-radius-md)' } }}
                {...form.getInputProps('wikiId')}
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
                placeholder={t('core:ui.home.enterPageTitle')}
                data-autofocus
                {...form.getInputProps('pageTitle')}
              />
            </Grid.Col>
            <Grid.Col
              span={{
                base: 12,
                md: 2,
                lg: 1.5,
              }}
            >
              <Button type="submit" variant="filled" size="lg" radius="md" w="100%">
                {t('core:ui.home.check')}
              </Button>
            </Grid.Col>
          </Grid>
        </form>
      </Stack>
    </Container>
  );
}
