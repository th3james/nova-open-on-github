import { injectable, inject } from "tsyringe";

import { IdeContext } from "./ide_context/ide_context";

@injectable()
export class GithubOpener {
  constructor(@inject("ideContext") private ideContext: IdeContext) {}

  openCurrentFileOnGithub(): void {
    const currentFile = this.ideContext.getCurrentFile();
    //     const gitRemote = this.gitContext.getGitRemote(currentFile);
    //
    //     const githubUrl = GithubUrlBuilder().build(gitRemote, currentFile);
  }
}
