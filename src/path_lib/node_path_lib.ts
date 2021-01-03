import { dirname } from "path";

import { PathLib } from "./path_lib";

export class NodePathLib implements PathLib {
  dirname(filePath: string): string {
    return dirname(filePath);
  }
}
