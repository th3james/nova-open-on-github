import { ExtensionConfig } from "./extension_config";

export class NovaExtensionConfig implements ExtensionConfig {
  getGitBinaryPath(): string {
    return "/usr/bin/git";
  }
}
