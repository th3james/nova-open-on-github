import test from "ava";

import { GitRemote } from "../git/git_remote";
import { GithubUrlBuilder } from "./github_url_builder";

test("GithubUrlBuilder.buildUrl given a valid remote and file name has a guess", (t) => {
  const validRemote = new GitRemote("robot");
  const fileName = "/hat/boat/robot.txt";

  const result = new GithubUrlBuilder().buildUrl(validRemote, fileName);

  t.is(result, `https://github.com/th3james/${validRemote.name}${fileName}`);
});
