import { NovaExtensionConfig } from "../extension_config/nova_extension_config";
import { NovaIdeContext } from "../ide_context/nova_ide_context";
import { NovaPathLib } from "../path_lib/nova_path_lib";
import { NovaProcessRunner } from "../process_runner/nova_process_runner";
import type { processBuilder } from "../process_runner/nova_process_runner";
import { NovaUrlOpener } from "../url_actions/nova_url_opener";

import { spawnCommonContainer } from "./common";

export const productionContainer = spawnCommonContainer();

const novaIdeContext = new NovaIdeContext(nova);
productionContainer.register("ideContext", { useValue: novaIdeContext });

const novaProcessBuilder: processBuilder = (command, options) =>
  new Process(command, options);
const novaProcessRunner = new NovaProcessRunner(novaProcessBuilder);
productionContainer.register("processRunner", { useValue: novaProcessRunner });

const novaExtensionConfig = new NovaExtensionConfig();
productionContainer.register("extensionConfig", {
  useValue: novaExtensionConfig,
});

const novaUrlOpener = new NovaUrlOpener();
productionContainer.register("urlOpener", {
  useValue: novaUrlOpener,
});

productionContainer.register("pathLib", {
  useValue: new NovaPathLib(nova.path),
});
