import test from "ava";

import { GitRemote } from "./git_remote";

test("GitRemote.parseFromString given a valid remote with a simple name parses the name", (t) => {
  const validRemote = "git@github.com:owner-name/simple.git";
  t.is(GitRemote.parseFromString(validRemote).name, "simple");
});

test("GitRemote.parseFromString given a valid remote with trailing whitespace name parses the name", (t) => {
  const validRemote = "git@github.com:owner-name/spaced.git   \n";
  t.is(GitRemote.parseFromString(validRemote).name, "spaced");
});

test("GitRemote.parseFromString given a valid remote with additional dot chars name parses the name", (t) => {
  const validRemote = "git@github.com:owner-name/www.website.nice.git";
  t.is(GitRemote.parseFromString(validRemote).name, "www.website.nice");
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
