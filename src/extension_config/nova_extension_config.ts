import { ExtensionConfig } from "./extension_config";

export class NovaExtensionConfig implements ExtensionConfig {
  getGitBinaryPath(): string {
    throw new Error("Not implemented");
  }
}
