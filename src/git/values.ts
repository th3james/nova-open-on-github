export class GitRemote {
  constructor(readonly name: string) {}

  static parseFromString(remoteString: string): GitRemote {
    return new GitRemote("Robot");
  }
}
