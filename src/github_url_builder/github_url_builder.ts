import { GitRemote } from "../git/git_remote";

export class GithubUrlBuilder {
  buildUrl(gitRemote: GitRemote, branch: string, filePath: string): string {
    return `https://github.com/${gitRemote.owner.trim()}/${gitRemote.name.trim()}/blob/${branch}/${filePath}`;
  }
}
