export class InvalidRemoteStringError extends Error {}

export class GitRemote {
  constructor(readonly name: string) {}

  static parseFromString(remoteString: string): GitRemote {
    const repoName = remoteString.split("/")[1];
    if (repoName) {
      return new GitRemote(repoName.split(".")[0]);
    } else {
      throw new InvalidRemoteStringError(
        `Cannot parse remote "${remoteString}"`
      );
    }
  }
}
