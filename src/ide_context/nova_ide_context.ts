import { IdeContext } from "./ide_context";

export class NovaIdeContext implements IdeContext {
  getCurrentFile(): string {
    throw new Error("not implemented");
  }
}
