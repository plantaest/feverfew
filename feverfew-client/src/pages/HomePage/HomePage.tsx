import { Button, Center, Container, Grid, Select, Stack, TextInput } from '@mantine/core';
import { useDocumentTitle, useFocusTrap } from '@mantine/hooks';
import { useTranslation } from 'react-i18next';
import { useForm } from '@mantine/form';
import { useNavigate } from 'react-router-dom';
import * as v from 'valibot';
import { valibotResolver } from 'mantine-form-valibot-resolver';
import { errorMessage } from '@/utils/errorMessage';
import { appState } from '@/states/appState';
import { wikiIds } from '@/utils/wikiIds';
import { Logo } from '@/components/Logo/Logo';
import classes from './HomePage.module.css';

const formSchema = (t: (key: string) => string) =>
  v.object({
    wikiId: v.pipe(v.string(t(errorMessage.notEmpty)), v.minLength(1, t(errorMessage.notEmpty))),
    pageTitle: v.pipe(v.string(), v.trim(), v.minLength(1, t(errorMessage.notEmpty))),
  });

type FormValues = v.InferOutput<ReturnType<typeof formSchema>>;

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
    validate: valibotResolver(formSchema(t)),
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
        <Center className={classes.logo}>
          <Logo />
        </Center>
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
