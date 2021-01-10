export class InvalidRemoteStringError extends Error {}

export class GitRemote {
  constructor(readonly name: string, readonly owner: string) {}

  static parseFromString(remoteString: string): GitRemote {
    const stringWithoutPrefix = remoteString.split(":")[1];

    if (stringWithoutPrefix) {
      const [ownerName, repoName] = stringWithoutPrefix.split("/");
      if (repoName) {
        return new GitRemote(repoName.split(".")[0], ownerName);
      }
    }

    throw new InvalidRemoteStringError(`Cannot parse remote "${remoteString}"`);
  }
}
