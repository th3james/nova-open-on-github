import test from "ava";

import { GitRemote } from "./git_remote";

test("GitRemote.parseFromString given a valid remote parses the name", (t) => {
  const validRemote = "git@github.com:owner-name/repo-name.git";
  t.is(GitRemote.parseFromString(validRemote).name, "repo-name");
});

test("GitRemote.parseFromString given a valid remote parses the owner", (t) => {
  const validRemote = "git@github.com:owner-name/repo-name.git";
  t.is(GitRemote.parseFromString(validRemote).owner, "owner-name");
});

test("GitRemote.parseFromString given an invalid remote raises an error", (t) => {
  t.throws(() => GitRemote.parseFromString("garbage"), {
    message: 'Cannot parse remote "garbage"',
  });
});
