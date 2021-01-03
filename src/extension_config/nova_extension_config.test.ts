import test from "ava";

import { NovaExtensionConfig } from "./nova_extension_config";

test("NovaExtensionConfig.getGitBinary always returns /usr/bin/git", (t) => {
  t.is(new NovaExtensionConfig().getGitBinaryPath(), "/usr/bin/git");
});
