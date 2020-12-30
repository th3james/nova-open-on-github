import { injectable, inject } from "tsyringe";

import { IdeContext } from "./ide_context/ide_context";
import { GitContext } from "./git/git_context";
import { GithubUrlBuilder } from "./github_url_builder/github_url_builder";
import { UrlOpener } from "./url_actions/url_opener";

@injectable()
export class GithubOpener {
  constructor(
    @inject("ideContext") private ideContext: IdeContext,
    @inject("gitContext") private gitContext: GitContext,
    @inject("urlOpener") private urlOpener: UrlOpener
  ) {}

  openCurrentFileOnGithub(): void {
    const currentFile = this.ideContext.getCurrentFile();
    const gitRemote = this.gitContext.getRemote(currentFile);

    const url = new GithubUrlBuilder().buildUrl(gitRemote, currentFile);
    this.urlOpener.openUrl(url);
  }
}
