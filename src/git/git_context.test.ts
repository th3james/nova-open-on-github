import "reflect-metadata";
import test from "ava";
import { deepEqual, instance, mock, when } from "ts-mockito";

import { GitContext } from "./git_context";
import { GitRemote } from "./git_remote";
import { GitCommandRunner } from "./git_command_runner";
import { NodePathLib } from "../path_lib/node_path_lib";

test("GitContext.getRemote for a file in a valid github repo queries git and parses a remote", async (t) => {
  const fileDir = "/some/where";
  const fileName = "file.txt";
  const filePath = `${fileDir}/${fileName}`;

  const rawRemoteString = `git@github.com:timmy/robot.git`;

  const mockGitCommandRunner = mock<GitCommandRunner>();
  when(
    mockGitCommandRunner.run(
      deepEqual(["config", "--get", "remote.origin.url"]),
      fileDir
    )
  ).thenResolve(rawRemoteString);

  const gitContext = new GitContext(
    instance(mockGitCommandRunner),
    new NodePathLib()
  );
  const result = await gitContext.getRemote(filePath);

  t.deepEqual(result, GitRemote.parseFromString(rawRemoteString));
});

test("GitContext.chrootFilePath given a filepath in a git repo it chroots it to the git root", async (t) => {
  const gitRoot = "/Volumes/Macintosh HD/Users/cool-dude/code/nice-project";
  const filePathInGit = "whatever/file.lol";
  const fullFilePath = `${gitRoot}/${filePathInGit}`;

  const pathLib = new NodePathLib();

  const mockGitCommandRunner = mock<GitCommandRunner>();
  when(
    mockGitCommandRunner.run(
      deepEqual(["rev-parse", "--show-toplevel"]),
      pathLib.dirname(fullFilePath)
    )
  ).thenResolve(gitRoot + "\n");

  const gitContext = new GitContext(instance(mockGitCommandRunner), pathLib);
  const result = await gitContext.chrootFilePath(fullFilePath);

  t.is(result, filePathInGit);
});

test("GitContext.getCurrentBranch given a filepath in a git repo it returns the current path", async (t) => {
  const branchName = "hatboat";
  const filePath = "/some/file/path.txt";

  const pathLib = new NodePathLib();

  const mockGitCommandRunner = mock<GitCommandRunner>();
  when(
    mockGitCommandRunner.run(
      deepEqual(["rev-parse", "--abbrev-ref", "HEAD"]),
      pathLib.dirname(filePath)
    )
  ).thenResolve(branchName + "\n");

  const gitContext = new GitContext(instance(mockGitCommandRunner), pathLib);

  const result = await gitContext.getCurrentBranch(filePath);

  t.is(result, branchName);
});
