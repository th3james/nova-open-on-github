#!/usr/bin/env sh

npx shadow-cljs compile test && node tmp/test.js