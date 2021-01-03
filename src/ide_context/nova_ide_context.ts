import { IdeContext } from "./ide_context";

export interface Nova {
  workspace: Workspace;
}

export class NovaIdeContext implements IdeContext {
  constructor(private nova: Nova) {}

  getCurrentFile(): string | null {
    return this.nova.workspace.activeTextEditor.document.path;
  }
}
