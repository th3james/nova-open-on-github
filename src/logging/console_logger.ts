import { Logger } from "./logger";

export class ConsoleLogger implements Logger {
  logError(error: Error) {
    console.log(error);
  }
}
