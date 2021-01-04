import { GitRemote } from "../git/git_remote";

export class GithubUrlBuilder {
  buildUrl(gitRemote: GitRemote, filePath: string): string {
    return `https://github.com/th3james/${gitRemote.name}${filePath}`;
  }
}
