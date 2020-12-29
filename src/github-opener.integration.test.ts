import test from "ava";
import { mock, verify, when } from "ts-mockito";

import { integrationContainer } from "./containers/integration";
import { UrlOpener } from "./url_actions/url_opener";
import { IdeContext } from "./ide_context/ide_context";
import { GitCommandRunner } from "./git/git_command_runner";
import { GithubOpener } from "./github-opener";

// Get repo base URL
// git rev-parse --show-toplevel
// Get origin URL
// git config --get remote.origin.url

test("Integration: GithubOpener given a file under source control opens it on github", (t) => {
  // Mock state
  const currentGitRoot = "/Users/cool-guy/nice-project";
  const currentRelativeFilePath = "thing/whatever.json";
  const currentFilePath = "${currentGitRoot}/${currentRelativeFilePath}";
  const gitOrigin = "git@github.com:cool-guy/nice-project.git"

  const mockIdeContext = mock<IdeContext>();
  when(mockIdeContext.getCurrentFile()).thenReturn(currentFilePath);
  integrationContainer.register("ideContext", {
    useValue: mockIdeContext,
  });
  
  const mockGitCommandRunner = mock(GitCommandRunner);
  when(mockGitCommandRunner.getGitRoot(currentFilePath)).thenReturn(currentGitRoot);
  when(mockGitCommandRunner.getOrigin(currentFilePath)).thenReturn(gitOrigin);
  integrationContainer.register("gitCommandRunner", {
    useValue: mockGitCommandRunner
  })

  const mockUrlOpener = mock<UrlOpener>();
  integrationContainer.register("urlOpener", {
    useValue: mockUrlOpener,
  });

  const githubOpener = integrationContainer.resolve(GithubOpener);
  githubOpener.openCurrentFileOnGithub();

  const expectedUrl = `https://github.com/cool-guy/nice-project/blob/master/${currentRelativeFilePath}`;

  verify(mockUrlOpener.openUrl(expectedUrl)).once();
});
