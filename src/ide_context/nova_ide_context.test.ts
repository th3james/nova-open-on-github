import test from "ava";

import { Nova, NovaIdeContext } from "./nova_ide_context";

test("NovaIdeContext.getCurrentFile when there is a current file returns the path", (t) => {
  const currentFilePath = "/whatever/thing.txt";

  const mockNova = {
    workspace: { activeTextEditor: { document: { path: currentFilePath } } },
  } as Nova;

  const result = new NovaIdeContext(mockNova).getCurrentFile();

  t.is(result, currentFilePath);
});
