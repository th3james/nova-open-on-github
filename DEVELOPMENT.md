# Development guide

The project is written in ClojureScript then compiled as a Node.js library, which we can load in the JavaScriptCore interpreter used by Nova.

## Installation pre-requisites

- Install a JDK and Node.js
- `npm install` the shadow-cljs build dependencies

## Building a release

```sh
npx shadow-cljs release extension
```
