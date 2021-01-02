import { UrlOpener } from "./url_opener";

export class NovaUrlOpener implements UrlOpener {
  openUrl(url: string): void {
    throw new Error("not implemented");
  }
}
