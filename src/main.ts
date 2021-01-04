import { productionContainer } from "./containers/nova";
import { GithubOpener } from "./github-opener";

exports.activate = function () {
  // Do work when the extension is activated
};

exports.deactivate = function () {
  // Clean up state before the extension is deactivated
};

const githubOpener = productionContainer.resolve(GithubOpener);

nova.commands.register(
  "open-on-github.openCurrentFile",
  githubOpener.openCurrentFileOnGithub,
  githubOpener
);
