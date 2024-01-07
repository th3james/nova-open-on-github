## Version 2.3

- Actually fix ssh:// urls
    - https://github.com/th3james/nova-open-on-github/issues/3

## Version 2.2

- Fix handling for ssh:// prefixed origin schemes (thanks again to @cartpauj for the report)
    - https://github.com/th3james/nova-open-on-github/issues/3

## Version 2.1

- Handling for https origin schemes (thanks to @cartpauj for the report)
    - https://github.com/th3james/nova-open-on-github/issues/3

## Version 2.0

Total rewrite of the project in ClojureScript.

- Faster performance due to a better concurrency model
- User $PATH is now respected, so `git` is no longer required to be in `/usr/bin/git`

## Version 1.0

Initial release
