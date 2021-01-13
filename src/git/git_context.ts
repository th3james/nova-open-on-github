import { injectable, inject } from "tsyringe";

import { GitRemote } from "./git_remote";
import { ExtensionConfig } from "../extension_config/extension_config";
import { PathLib } from "../path_lib/path_lib";
import { ProcessRunner } from "../process_runner/process_runner";

@injectable()
export class GitContext {
  constructor(
    @inject("processRunner") private processRunner: ProcessRunner,
    @inject("extensionConfig") private extensionConfig: ExtensionConfig,
    @inject("pathLib") private pathLib: PathLib
  ) {}

  async getRemote(filePath: string): Promise<GitRemote> {
    const fileDir = this.pathLib.dirname(filePath);
    const remoteString = await this.processRunner.runCommand(
      this.extensionConfig.getGitBinaryPath(),
      ["config", "--get", "remote.origin.url"],
      fileDir
    );

    return GitRemote.parseFromString(remoteString);
  }

  async chrootFilePath(filePath: string): Promise<string> {
    const gitRoot = (
      await this.processRunner.runCommand(
        this.extensionConfig.getGitBinaryPath(),
        ["rev-parse", "--show-toplevel"],
        this.pathLib.dirname(filePath)
      )
    ).trim();
    return filePath.split(gitRoot + "/")[1];
  }
}
