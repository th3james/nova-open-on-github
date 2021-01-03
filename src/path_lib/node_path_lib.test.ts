import test from "ava";

import { NodePathLib } from "./node_path_lib";

test("NodePathLib.dirname given a valid file path returns the containing directory", (t) => {
  t.is(new NodePathLib().dirname("/hat/boat/robot.txt"), "/hat/boat");
});
