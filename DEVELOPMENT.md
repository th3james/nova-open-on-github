# Development guide

The project is written in ClojureScript then compiled as a Node.js library, which we can load in the JavaScriptCore interpreter used by Nova.

## Installation pre-requisites

- Install a JDK and Node.js
- `npm install` the shadow-cljs build dependencies

## Development commands

```sh
# Build a release
scripts/./build-release.sh
# Start a ClojureScript REPL
scripts/./start-repl.sh
# Run the tests
scripts/./run-tests.sh
```
