import { ProcessRunner } from "./process_runner";

export class NovaProcessRunner implements ProcessRunner {
  runCommand(): string {
    throw new Error("not implemented");
  }
}
