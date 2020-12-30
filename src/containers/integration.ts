import "reflect-metadata";

import { container } from "tsyringe";

import { GitContext } from "../git/git_context";

const integrationContainer = container;

integrationContainer.register("gitContext", {
  useClass: GitContext,
});

export { integrationContainer };
