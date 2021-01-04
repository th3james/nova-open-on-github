import { UrlOpener } from "./url_opener";

export interface Nova {
  openURL(url: string, callback?: (success: boolean) => void): void;
}

export class NovaUrlOpener implements UrlOpener {
  constructor(private nova: Nova) {}

  openUrl(url: string): void {
    console.log("opening url");
    console.log(url);
    this.nova.openURL(url);
    console.log("opened");
  }
}
