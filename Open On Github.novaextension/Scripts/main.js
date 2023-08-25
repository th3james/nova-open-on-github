// JavaScriptCore doesn't expose a `global` var like node.js
// This emulates it
globalThis.global = globalThis;

console.log("Loading Open On GitHub extension");
const core = require("core.js");

core();
console.log("Finished loading Open on GitHub extension");
