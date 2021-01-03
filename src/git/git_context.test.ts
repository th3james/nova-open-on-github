import "reflect-metadata";
import test from "ava";
import {
  anything,
  capture,
  deepEqual,
  instance,
  mock,
  verify,
  when,
} from "ts-mockito";

import { GitContext } from "./git_context";
import { GitRemote } from "./git_remote";
import { ExtensionConfig } from "../extension_config/extension_config";
import { ProcessRunner } from "../process_runner/process_runner";
import { NodePathLib } from "../path_lib/node_path_lib";

test("GitContext.getRemote for a file in a valid github repo queries git and parses a remote", (t) => {
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
  ).thenReturn(rawRemoteString);

  const gitContext = new GitContext(
    instance(mockProcessRunner),
    instance(mockExtensionConfig),
    new NodePathLib()
  );
  const result = gitContext.getRemote(filePath);

  t.deepEqual(result, GitRemote.parseFromString(rawRemoteString));
});
