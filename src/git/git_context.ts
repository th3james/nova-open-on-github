import { GitRemote } from "./values";

export class GitContext {
  getRemote(filePath: string): GitRemote {
    throw new Error("Not implemented");
  }
}
