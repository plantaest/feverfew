import { underscoreTitle } from '@/utils/underscoreTitle';

export namespace MwHelper {
  export const createPageUri = (serverName: string, pageTitle: string) =>
    `//${serverName}/wiki/${underscoreTitle(pageTitle)}`;
}
