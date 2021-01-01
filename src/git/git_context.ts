import { injectable, inject } from "tsyringe";

import { GitRemote } from "./git_remote";
import { ExtensionConfig } from "../extension_config/extension_config";
import { ProcessRunner } from "../process_runner/process_runner";

@injectable()
export class GitContext {
  constructor(
    @inject("processRunner") private processRunner: ProcessRunner,
    @inject("extensionConfig") private extensionConfig: ExtensionConfig
  ) {}

  getRemote(filePath: string): GitRemote {
    const remoteString = this.processRunner.runCommand(
      this.extensionConfig.getGitBinaryPath(),
      ["config", "--get", "remote.origin.url"],
      "/some/where"
    );

    return GitRemote.parseFromString(remoteString);
  }
}
