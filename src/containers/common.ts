import "reflect-metadata";

import { container } from "tsyringe";
import type { DependencyContainer } from "tsyringe";

import { GitContext } from "../git/git_context";
import { GitCommandRunner } from "../git/git_command_runner";

const commonContainer = container;

commonContainer.register("gitContext", {
  useClass: GitContext,
});
commonContainer.register("gitCommandRunner", {
  useClass: GitCommandRunner,
});

export function spawnCommonContainer(): DependencyContainer {
  return commonContainer.createChildContainer();
}
