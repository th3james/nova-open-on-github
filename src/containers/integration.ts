import "reflect-metadata";

import { container } from "tsyringe";

import { spawnCommonContainer } from "./common";
import { NodePathLib } from "../path_lib/node_path_lib";

const integrationContainer = spawnCommonContainer();

container.register("pathLib", { useValue: new NodePathLib() });

export { integrationContainer };
