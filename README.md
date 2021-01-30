# Open on GitHub - Nova Extension

A [Nova](https://nova.app) extension for opening files on GitHub. Heavily inspired by `tpope`'s excellent [vim-fugitive](https://github.com/tpope/vim-fugitive).

## Features

### Supported

- Opening the current file on GitHub

### Planned

- Open current line/selection on GitHub
- Configurability of git binary path (`/usr/bin/git` is assumed, which is where Apple Command Line Tools put it)

Leave a comment or issue to vote or request features.

## Development

The extension is written in TypeScript, and built with rollup

```shell
npm run-script build
```

There is an extensive test suite, which you can run with

```shell
npm t
```
