import { UrlOpener } from "./url_opener";

export interface Nova {
  openURL(url: string, callback?: (success: boolean) => void): void;
}

export class NovaUrlOpener implements UrlOpener {
  constructor(private nova: Nova) {}

  openUrl(url: string): Promise<void> {
    return new Promise((resolve, reject) => {
      this.nova.openURL(url, (success) => {
        if (success) {
          resolve();
        } else {
          reject(new Error(`Couldn't open URL ${url}`));
        }
      });
    });
  }
}
