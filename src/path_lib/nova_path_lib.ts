import { PathLib } from "./path_lib";

export class NovaPathLib implements PathLib {
  constructor(private novaPath: Path) {}

  dirname(filePath: string): string {
    return this.novaPath.dirname(filePath);
  }
}
