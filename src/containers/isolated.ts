import "reflect-metadata";

import { container } from "tsyringe";
import type { DependencyContainer } from "tsyringe";

export function spawnIsolatedContainer(): DependencyContainer {
  return container.createChildContainer();
}
