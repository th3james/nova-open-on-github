import test from "ava";
import { anything, instance, mock, spy, verify, when } from "ts-mockito";

import { NovaProcessRunner } from "./nova_process_runner";
import type { processBuilder } from "./nova_process_runner";

test("NovaProcessRunner.runCommand given a valid command spawns a process and returns STDOUT", async (t) => {
  const commandBinary = "/usr/bin/wut";
  const commandArgs = ["a", "b"];
  const commandWorkingDir = "/some/folder";

  const commandOutput = "whatever output";

  const mockProcess = mock<Process>();
  when(mockProcess.onStdout(anything())).thenCall(
    (cb: (x: string) => void): any => {
      cb(commandOutput);
    }
  );
  when(mockProcess.onDidExit(anything())).thenCall(
    (cb: (status: number) => void): void => {
      cb(0);
    }
  );

  const mockProcessBuilder: processBuilder = (actualCommand, options) => {
    t.is(actualCommand, commandBinary);
    t.is(options.args, commandArgs);
    t.is(options.cwd, commandWorkingDir);
    return instance(mockProcess);
  };

  const novaProcessRunner = new NovaProcessRunner(mockProcessBuilder);
  const result = await novaProcessRunner.runCommand(
    commandBinary,
    commandArgs,
    commandWorkingDir
  );

  verify(mockProcess.start()).once();
  t.is(result, commandOutput);
});

test("NovaProcessRunner.runCommand given a failing command spawns a process, and rejects with STDERR", async (t) => {
  const errorMessage = "Whoops";

  const mockProcess = mock<Process>();
  when(mockProcess.onStderr(anything())).thenCall(
    (cb: (x: string) => void): any => {
      cb(errorMessage);
    }
  );
  when(mockProcess.onDidExit(anything())).thenCall(
    (cb: (status: number) => void): void => {
      cb(1);
    }
  );

  const mockProcessBuilder = () => instance(mockProcess);

  const novaProcessRunner = new NovaProcessRunner(mockProcessBuilder);
  const error = await t.throwsAsync(novaProcessRunner.runCommand("a", [], "c"));

  verify(mockProcess.start()).once();
  t.is(error.message, errorMessage);
});
