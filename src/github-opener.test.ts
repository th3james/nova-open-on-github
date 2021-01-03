import test from "ava";
import { anything, capture, instance, mock, verify, when } from "ts-mockito";

import { spawnIsolatedContainer } from "./containers/isolated";
import { GitContext } from "./git/git_context";
import { GitRemote } from "./git/git_remote";
import { GithubOpener } from "./github-opener";
import { GithubUrlBuilder } from "./github_url_builder/github_url_builder";
import { IdeContext } from "./ide_context/ide_context";
import { UrlOpener } from "./url_actions/url_opener";

test("GithubOpener.openCurrentFile with a valid file and branch opens the correct URL", (t) => {
  const container = spawnIsolatedContainer();
  const gitRemote = new GitRemote("hat");
  const currentFilePath = "/some/path.txt";

  const mockIdeContext = mock<IdeContext>();
  when(mockIdeContext.getCurrentFile()).thenReturn(currentFilePath);
  container.register("ideContext", {
    useValue: instance(mockIdeContext),
  });

  const mockGitContext = mock(GitContext);
  when(mockGitContext.getRemote(currentFilePath)).thenReturn(gitRemote);
  container.register("gitContext", {
    useValue: instance(mockGitContext),
  });

  const mockUrlOpener = mock<UrlOpener>();
  container.register("urlOpener", {
    useValue: instance(mockUrlOpener),
  });

  const expectedUrl = new GithubUrlBuilder().buildUrl(
    gitRemote,
    currentFilePath
  );

  const githubOpener = container.resolve(GithubOpener);

  githubOpener.openCurrentFileOnGithub();

  verify(mockUrlOpener.openUrl(expectedUrl)).once();
  t.pass();
});

test("GithubOpener.openCurrentFile without a current file logs it does nothing", (t) => {
  const container = spawnIsolatedContainer();

  const mockIdeContext = mock<IdeContext>();
  when(mockIdeContext.getCurrentFile()).thenReturn(null);
  container.register("ideContext", {
    useValue: instance(mockIdeContext),
  });
  container.register("gitContext", {
    useValue: instance(mock<GitContext>()),
  });
  container.register("urlOpener", {
    useValue: instance(mock<UrlOpener>()),
  });

  const githubOpener = container.resolve(GithubOpener);

  githubOpener.openCurrentFileOnGithub();

  t.pass();
});
