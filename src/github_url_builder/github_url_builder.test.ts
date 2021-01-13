import test from "ava";

import { GitRemote } from "../git/git_remote";
import { GithubUrlBuilder } from "./github_url_builder";

test("GithubUrlBuilder.buildUrl given a valid remote and relative file name it builds the correct URL", (t) => {
  const validRemote = new GitRemote("robot", "some-dude");
  const fileName = "boat/robot.txt";

  const result = new GithubUrlBuilder().buildUrl(validRemote, fileName);

  t.is(
    result,
    `https://github.com/${validRemote.owner}/${validRemote.name}/${fileName}`
  );
});
