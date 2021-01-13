import { injectable, inject } from "tsyringe";

import { IdeContext } from "./ide_context/ide_context";
import { GitContext } from "./git/git_context";
import { GithubUrlBuilder } from "./github_url_builder/github_url_builder";
import { Logger } from "./logging/logger";
import { UrlOpener } from "./url_actions/url_opener";

@injectable()
export class GithubOpener {
  constructor(
    @inject("ideContext") private ideContext: IdeContext,
    @inject("gitContext") private gitContext: GitContext,
    @inject("urlOpener") private urlOpener: UrlOpener,
    @inject("logger") private logger: Logger
  ) {}

  async openCurrentFileOnGithub(): Promise<void> {
    const currentFile = this.ideContext.getCurrentFile();
    if (currentFile === null) {
      this.logger.logError(new Error("No current file, can't open on GitHub"));
      return;
    }
    const chrootedFile = await this.gitContext.chrootFilePath(currentFile);

    const gitRemote = await this.gitContext.getRemote(currentFile);

    const url = new GithubUrlBuilder().buildUrl(
      gitRemote,
      "master",
      chrootedFile
    );
    try {
      await this.urlOpener.openUrl(url);
    } catch (err) {
      this.logger.logError(err);
    }
  }
}
