import { underscoreTitle } from '@/utils/underscoreTitle';

export namespace MwHelper {
  export const createRevisionUri = (serverName: string, pageTitle: string, revisionId: number) =>
    `//${serverName}/w/index.php?title=${underscoreTitle(pageTitle)}&oldid=${revisionId}`;
}
