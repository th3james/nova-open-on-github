import { injectable, inject } from "tsyringe";

import { GitRemote } from "./git_remote";
import { GitCommandRunner } from "./git_command_runner";
import { PathLib } from "../path_lib/path_lib";

@injectable()
export class GitContext {
  constructor(
    @inject("gitCommandRunner") private gitCommandRunner: GitCommandRunner,
    @inject("pathLib") private pathLib: PathLib
  ) {}

  async getRemote(filePath: string): Promise<GitRemote> {
    const fileDir = this.pathLib.dirname(filePath);
    const remoteString = await this.gitCommandRunner.run(
      ["config", "--get", "remote.origin.url"],
      fileDir
    );

    return GitRemote.parseFromString(remoteString);
  }

  async chrootFilePath(filePath: string): Promise<string> {
    const gitRoot = (
      await this.gitCommandRunner.run(
        ["rev-parse", "--show-toplevel"],
        this.pathLib.dirname(filePath)
      )
    ).trim();
    return filePath.split(gitRoot + "/")[1];
  }

  async getCurrentBranch(filePath: string): Promise<string> {
    return (
      await this.gitCommandRunner.run(
        ["rev-parse", "--abbrev-ref", "HEAD"],
        this.pathLib.dirname(filePath)
      )
    ).trim();
  }
}
