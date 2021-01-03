import test from "ava";
import { instance, mock, when } from "ts-mockito";

import { NovaPathLib } from "./nova_path_lib";

test("NovaPathLib.dirname given a valid file path returns the containing directory", (t) => {
  const filePath = "/whatever/file.txt";
  const mockPath = mock<Path>();
  when(mockPath.dirname(filePath)).thenReturn("/whatever");

  t.is(new NovaPathLib(instance(mockPath)).dirname(filePath), "/whatever");
});
