import { injectable, inject } from "tsyringe";

import { GitRemote } from "./git_remote";
import { ProcessRunner } from "../process_runner/process_runner";

@injectable()
export class GitContext {
  constructor(@inject("ProcessRunner") private processRunner: ProcessRunner) {}

  getRemote(filePath: string): GitRemote {
    const remoteString = this.processRunner.runCommand("YOLO", [""], "");

    return GitRemote.parseFromString(remoteString);
  }
}
