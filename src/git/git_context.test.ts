import "reflect-metadata";
import test from "ava";
import { deepEqual, instance, mock, when } from "ts-mockito";

import { GitContext } from "./git_context";
import { GitRemote } from "./git_remote";
import { ExtensionConfig } from "../extension_config/extension_config";
import { ProcessRunner } from "../process_runner/process_runner";
import { NodePathLib } from "../path_lib/node_path_lib";

test("GitContext.getRemote for a file in a valid github repo queries git and parses a remote", async (t) => {
  const gitBinaryPath = "/usr/bin/gitz";
  const fileDir = "/some/where";
  const fileName = "file.txt";
  const filePath = `${fileDir}/${fileName}`;

  const rawRemoteString = `git@github.com:timmy/robot.git`;

  const mockExtensionConfig = mock<ExtensionConfig>();
  when(mockExtensionConfig.getGitBinaryPath()).thenReturn(gitBinaryPath);

  const mockProcessRunner = mock<ProcessRunner>();
  when(
    mockProcessRunner.runCommand(
      gitBinaryPath,
      deepEqual(["config", "--get", "remote.origin.url"]),
      fileDir
    )
  ).thenResolve(rawRemoteString);

  const gitContext = new GitContext(
    instance(mockProcessRunner),
    instance(mockExtensionConfig),
    new NodePathLib()
  );
  const result = await gitContext.getRemote(filePath);

  t.deepEqual(result, GitRemote.parseFromString(rawRemoteString));
});

test("GitContext.chrootFilePath given a filepath in a git repo it chroots it to the git root", async (t) => {
  const gitRoot = "/some/path";
  const filePathInGit = "whatever/file.lol";
  const fullFilePath = `${gitRoot}/${filePathInGit}`;
  const gitBinaryPath = "/bin/git";

  const pathLib = new NodePathLib();

  const mockExtensionConfig = mock<ExtensionConfig>();
  when(mockExtensionConfig.getGitBinaryPath()).thenReturn(gitBinaryPath);

  const mockProcessRunner = mock<ProcessRunner>();
  when(
    mockProcessRunner.runCommand(
      gitBinaryPath,
      deepEqual(["rev-parse", "--show-toplevel"]),
      pathLib.dirname(fullFilePath)
    )
  ).thenResolve(gitRoot);

  const gitContext = new GitContext(
    instance(mockProcessRunner),
    instance(mockExtensionConfig),
    pathLib
  );
  const result = await gitContext.chrootFilePath(fullFilePath);

  t.deepEqual(result, filePathInGit);
});
