import test from "ava";
import { anything, instance, mock, verify, when } from "ts-mockito";

import { NovaUrlOpener, Nova } from "./nova_url_opener";

test("NovaUrlOpener.openUrl given a valid url opens it and resolves", async (t) => {
  const url = "https://docs.nova.app";
  const mockedNova = mock<Nova>();
  when(mockedNova.openURL(url, anything())).thenCall(
    (_: string, cb: (x: boolean) => void) => {
      cb(true);
    }
  );

  await new NovaUrlOpener(instance(mockedNova)).openUrl(url);

  t.pass();
});

test("NovaUrlOpener.openUrl given an invalid url it rejects", async (t) => {
  const url = "https://docs.nova.app";
  const mockedNova = mock<Nova>();
  when(mockedNova.openURL(url, anything())).thenCall(
    (_: string, cb: (x: boolean) => void) => {
      cb(false);
    }
  );

  await t.throwsAsync(new NovaUrlOpener(instance(mockedNova)).openUrl(url));
});
