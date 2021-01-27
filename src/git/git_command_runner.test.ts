import "reflect-metadata";
import test from "ava";
import { deepEqual, instance, mock, when } from "ts-mockito";

import { ExtensionConfig } from "../extension_config/extension_config";
import { ProcessRunner } from "../process_runner/process_runner";

import { GitCommandRunner } from "./git_command_runner";

test("GitCommandRunner.run given args and a working dir runs the command and returns the result", async (t) => {
  const args = ["a", "b"];
  const workingDir = "/hat/board/";
  const gitBinaryPath = "/usr/bin/git";
  const fakeProcessResponse = "nice robot";

  const mockExtensionConfig = mock<ExtensionConfig>();
  when(mockExtensionConfig.getGitBinaryPath()).thenReturn(gitBinaryPath);

  const mockProcessRunner = mock<ProcessRunner>();
  when(
    mockProcessRunner.runCommand(gitBinaryPath, deepEqual(args), workingDir)
  ).thenResolve(fakeProcessResponse);

  const gitCommandRunner = new GitCommandRunner(
    instance(mockProcessRunner),
    instance(mockExtensionConfig)
  );

  const result = await gitCommandRunner.run(args, workingDir);
  t.is(fakeProcessResponse, result);
});
