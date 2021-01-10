import "reflect-metadata";
import test from "ava";
import { anything, deepEqual, instance, mock, verify, when } from "ts-mockito";

import { Logger } from "./logging/logger";
import { GitContext } from "./git/git_context";
import { GitRemote } from "./git/git_remote";
import { GithubOpener } from "./github-opener";
import { GithubUrlBuilder } from "./github_url_builder/github_url_builder";
import { IdeContext } from "./ide_context/ide_context";
import { UrlOpener } from "./url_actions/url_opener";

test("GithubOpener.openCurrentFile with a valid file and branch opens the correct URL", async (t) => {
  const gitRemote = new GitRemote("hat");
  const currentFilePath = "/some/path.txt";

  const mockIdeContext = mock<IdeContext>();
  when(mockIdeContext.getCurrentFile()).thenReturn(currentFilePath);

  const mockGitContext = mock(GitContext);
  when(mockGitContext.getRemote(currentFilePath)).thenResolve(gitRemote);

  const mockUrlOpener = mock<UrlOpener>();

  const expectedUrl = new GithubUrlBuilder().buildUrl(
    gitRemote,
    currentFilePath
  );

  const githubOpener = new GithubOpener(
    instance(mockIdeContext),
    instance(mockGitContext),
    instance(mockUrlOpener),
    instance(mock<Logger>())
  );

  await githubOpener.openCurrentFileOnGithub();

  verify(mockUrlOpener.openUrl(expectedUrl)).once();
  t.pass();
});

test("GithubOpener.openCurrentFile without a current file it logs the error", (t) => {
  const mockLogger = mock<Logger>();

  const mockIdeContext = mock<IdeContext>();
  when(mockIdeContext.getCurrentFile()).thenReturn(null);

  const githubOpener = new GithubOpener(
    instance(mockIdeContext),
    instance(mock<GitContext>()),
    instance(mock<UrlOpener>()),
    instance(mockLogger)
  );

  githubOpener.openCurrentFileOnGithub();

  verify(
    mockLogger.logError(
      deepEqual(new Error("No current file, can't open on GitHub"))
    )
  ).once();

  t.pass();
});

test("GithubOpener.openCurrentFile given an invalid url logs the error", async (t) => {
  const mockLogger = mock<Logger>();
  const theError = new Error("Bad URL");

  const mockIdeContext = mock<IdeContext>();
  when(mockIdeContext.getCurrentFile()).thenReturn("whatever");

  const mockGitContext = mock<GitContext>();
  when(mockGitContext.getRemote("whatever")).thenResolve(new GitRemote("a"));

  const mockUrlOpener = mock<UrlOpener>();
  when(mockUrlOpener.openUrl(anything())).thenReject(theError);

  const githubOpener = new GithubOpener(
    instance(mockIdeContext),
    instance(mockGitContext),
    instance(mockUrlOpener),
    instance(mockLogger)
  );

  await githubOpener.openCurrentFileOnGithub();

  verify(mockLogger.logError(theError)).once();
  t.pass();
});
