import test from "ava";
import { instance, mock, when } from "ts-mockito";

import { spawnIsolatedContainer } from "../containers/isolated";
import { GitContext } from "./git_context";
import { GitRemote } from "./git_remote";
import { ProcessRunner } from "../process_runner/process_runner";

test("GitContext.getRemote for a file in a valid github repo queries git and parses a remote", (t) => {
  const fileDir = "/some/where/";
  const fileName = "file.txt";
  const filePath = `${fileDir}/${fileName}`;

  const rawRemoteString = `git@github.com:th3james/robot.git`;

  const container = spawnIsolatedContainer();
  const mockProcessRunner = mock<ProcessRunner>();
  when(
    mockProcessRunner.runCommand(
      "/usr/bin/git",
      ["config", "--get", "remote.origin.url"],
      fileDir
    )
  ).thenReturn(rawRemoteString);
  container.register("ProcessRunner", {
    useValue: instance(mockProcessRunner),
  });

  const gitContext = container.resolve(GitContext);
  const result = gitContext.getRemote(filePath);

  t.deepEqual(result, GitRemote.parseFromString(rawRemoteString));
});
