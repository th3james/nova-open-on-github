import { ProcessRunner } from "./process_runner";

// We want to be able to pass a dummy Nova Process object in for testing,
// so this defines an interface which returns a Nova Process
export type processBuilder = (
  command: string,
  options: {
    args: string[];
    cwd?: string;
  }
) => Process;

export class NovaProcessRunner implements ProcessRunner {
  constructor(private processBuilder: processBuilder) {}
  async runCommand(
    command: string,
    args: string[],
    workingDirectory: string
  ): Promise<string> {
    const process = this.processBuilder(command, {
      args: args,
      cwd: workingDirectory,
    });

    const stdoutLines: string[] = [];
    process.onStdout((line) => {
      stdoutLines.push(line);
    });
    const stderrLines: string[] = [];
    process.onStderr((line) => {
      stderrLines.push(line);
    });

    process.start();

    return new Promise<string>((resolve, reject) => {
      process.onDidExit((status) => {
        if (status !== 0) {
          reject(new Error(stderrLines.join("\n")));
        } else {
          resolve(stdoutLines.join("\n"));
        }
      });
    });
  }
}
