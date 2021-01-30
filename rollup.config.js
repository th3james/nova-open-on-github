import { nodeResolve } from "@rollup/plugin-node-resolve";
import typescript from "@rollup/plugin-typescript";

export default {
  input: "src/main.ts",
  output: {
    dir: "Open on Github.novaextension/Scripts",
    format: "cjs",
  },
  plugins: [typescript(), nodeResolve()],
};
