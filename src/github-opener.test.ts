import test from "ava";
import { mock, verify, when } from "ts-mockito";

import { isolatedContainer } from "./containers/isolated";
import { GitContext } from "./git/git_context";
import { GitRemote } from "./git/values";
import { GithubOpener } from "./github-opener";
import { GithubUrlBuilder } from "./github_url_builder/github_url_builder";
import { IdeContext } from "./ide_context/ide_context";
import { UrlOpener } from "./url_actions/url_opener";

test("GithubOpener.openCurrentFile with a valid file and branch opens the correct URL", () => {
  const gitRemote = new GitRemote();
  const currentFilePath = "/some/path.txt";

  const mockIdeContext = mock<IdeContext>();
  when(mockIdeContext.getCurrentFile()).thenReturn(currentFilePath);
  isolatedContainer.register("ideContext", {
    useValue: mockIdeContext,
  });

  const mockGitContext = mock(GitContext);
  when(mockGitContext.getRemote(currentFilePath)).thenReturn(gitRemote);
  isolatedContainer.register("gitContext", {
    useValue: mockGitContext,
  });

  const mockUrlOpener = mock<UrlOpener>();
  isolatedContainer.register("urlOpener", {
    useValue: mockUrlOpener,
  });

  const expectedUrl = new GithubUrlBuilder().buildUrl(
    gitRemote,
    currentFilePath
  );

  const githubOpener = isolatedContainer.resolve(GithubOpener);
  githubOpener.openCurrentFileOnGithub();

  verify(mockUrlOpener.openUrl(expectedUrl)).once();
});
