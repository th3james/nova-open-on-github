import "reflect-metadata";

import { container } from "tsyringe";

import { GitContext } from "../git/git_context";
import { NodePathLib } from "../path_lib/node_path_lib";

const integrationContainer = container;

integrationContainer.register("gitContext", {
  useClass: GitContext,
});
container.register("pathLib", { useValue: new NodePathLib() });

export { integrationContainer };
