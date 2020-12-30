import { GitRemote } from "../git/values";

export class GithubUrlBuilder {
  buildUrl(gitRemote: GitRemote, filePath: string): string {
    return `${gitRemote.name}${filePath}`;
  }
}
