import test from "ava";
import { instance, mock, verify } from "ts-mockito";

import { NovaUrlOpener, Nova } from "./nova_url_opener";

test("NovaUrlOpener.openUrl given a valid url opens it", (t) => {
  const url = "https://docs.nova.app";
  const mockedNova = mock<Nova>();

  new NovaUrlOpener(instance(mockedNova)).openUrl(url);

  verify(mockedNova.openURL(url)).once();
  t.pass();
});
