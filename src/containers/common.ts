import "reflect-metadata";

import { container } from "tsyringe";
import type { DependencyContainer } from "tsyringe";

import { GitContext } from "../git/git_context";

const commonContainer = container;

commonContainer.register("gitContext", {
  useClass: GitContext,
});

export function spawnCommonContainer(): DependencyContainer {
  return commonContainer.createChildContainer();
}
