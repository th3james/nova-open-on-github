// JavaScriptCore doesn't expose a `global` var like node.js
// This emulates it
globalThis.global = globalThis;

const core = require("core.js");

core();
