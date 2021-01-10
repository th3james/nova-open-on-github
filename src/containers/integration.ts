import "reflect-metadata";

import { container } from "tsyringe";

import { spawnCommonContainer } from "./common";
import { NodePathLib } from "../path_lib/node_path_lib";
import { ConsoleLogger } from "../logging/console_logger";

const integrationContainer = spawnCommonContainer();

container.register("pathLib", { useValue: new NodePathLib() });
container.register("logger", { useValue: new ConsoleLogger() });

export { integrationContainer };
