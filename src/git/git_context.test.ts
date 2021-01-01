import test from "ava";
import { anything, capture, instance, mock, verify, when } from "ts-mockito";

import { spawnIsolatedContainer } from "../containers/isolated";
import { GitContext } from "./git_context";
import { GitRemote } from "./git_remote";
import { ExtensionConfig } from "../extension_config/extension_config";
import { ProcessRunner } from "../process_runner/process_runner";

test("GitContext.getRemote for a file in a valid github repo queries git and parses a remote", (t) => {
  const gitBinaryPath = "/usr/bin/gitz";
  const fileDir = "/some/where";
  const fileName = "file.txt";
  const filePath = `${fileDir}/${fileName}`;

  const rawRemoteString = `git@github.com:timmy/robot.git`;

  const container = spawnIsolatedContainer();

  const mockExtensionConfig = mock<ExtensionConfig>();
  when(mockExtensionConfig.getGitBinaryPath()).thenReturn(gitBinaryPath);
  container.register("extensionConfig", {
    useValue: instance(mockExtensionConfig),
  });

  const mockProcessRunner = mock<ProcessRunner>();
  when(
    mockProcessRunner.runCommand(gitBinaryPath, anything(), fileDir)
  ).thenCall((_, actualArgs) => {
    // ts-mockito can't handle matching arrays
    t.deepEqual(actualArgs, ["config", "--get", "remote.origin.url"]);
    return rawRemoteString;
  });
  container.register("processRunner", {
    useValue: instance(mockProcessRunner),
  });

  const gitContext = container.resolve(GitContext);
  const result = gitContext.getRemote(filePath);

  t.deepEqual(result, GitRemote.parseFromString(rawRemoteString));
});
