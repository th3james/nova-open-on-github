import test from "ava";
import { mock, verify } from "ts-mockito";

test("Integration: GithubOpener given a file under source control opens it on github", (t) => {
  const mockUrlOpener = mock<UrlOpener>();

  verify(
    mockUrlOpener.openUrl(
      "https://github.com/th3james/nova-open-on-github/blob/master/package.json"
    )
  ).once();
});
