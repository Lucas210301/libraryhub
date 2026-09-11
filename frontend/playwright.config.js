import { defineConfig } from "@playwright/test";

export default defineConfig({
  testDir: "./tests",
  // The suite drives a single backend and a single PostgreSQL database, so the
  // tests share mutable state and have to run one at a time.
  workers: 1,
  // The dev server compiles a route the first time it is requested, which can
  // take longer than the default budget on a busy machine.
  timeout: 60_000,
  use: {
    baseURL: "http://localhost:3000"
  },
  webServer: {
    command: "npm run dev",
    url: "http://localhost:3000",
    reuseExistingServer: true,
    timeout: 120_000
  }
});
