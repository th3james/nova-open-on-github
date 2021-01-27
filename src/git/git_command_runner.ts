import { injectable, inject } from "tsyringe";

import { ExtensionConfig } from "../extension_config/extension_config";
import { ProcessRunner } from "../process_runner/process_runner";

@injectable()
export class GitCommandRunner {
  constructor(
    @inject("processRunner") private processRunner: ProcessRunner,
    @inject("extensionConfig") private extensionConfig: ExtensionConfig
  ) {}

  async run(args: string[], workingDir: string): Promise<string> {
    return await this.processRunner.runCommand(
      this.extensionConfig.getGitBinaryPath(),
      args,
      workingDir
    );
  }
}
