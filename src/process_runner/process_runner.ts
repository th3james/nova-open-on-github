export interface ProcessRunner {
  runCommand(command: string, args: string[], workingDirectory: string): string;
}
