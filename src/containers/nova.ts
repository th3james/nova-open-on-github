import { NovaExtensionConfig } from "../extension_config/nova_extension_config";
import { NovaIdeContext } from "../ide_context/nova_ide_context";
import { NovaProcessRunner } from "../process_runner/nova_process_runner";
import { NovaUrlOpener } from "../url_actions/nova_url_opener";

import { integrationContainer } from "./integration";

export const productionContainer = integrationContainer.createChildContainer();

const novaIdeContext = new NovaIdeContext();
productionContainer.register("ideContext", { useValue: novaIdeContext });

const novaProcessRunner = new NovaProcessRunner();
productionContainer.register("processRunner", { useValue: novaProcessRunner });

const novaExtensionConfig = new NovaExtensionConfig();
productionContainer.register("extensionConfig", {
  useValue: novaExtensionConfig,
});

const novaUrlOpener = new NovaUrlOpener();
productionContainer.register("urlOpener", {
  useValue: novaUrlOpener,
});
