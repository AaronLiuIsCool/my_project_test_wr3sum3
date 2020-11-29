import * as Sentry from "@sentry/browser";
import { Integrations } from "@sentry/tracing";

Sentry.init({
  dsn: process.env.SENTRY_DSN || 'https://270864132b0845e4a9ae4f68f96c77c2@o434398.ingest.sentry.io/5391423',

  // To set your release version
  release: "kdr-webapp@" + process.env.npm_package_version,
  integrations: [new Integrations.BrowserTracing()],

  // Set tracesSampleRate to 1.0 to capture 100%
  // of transactions for performance monitoring.
  // We recommend adjusting this value in production
  tracesSampleRate: process.env.SENTRY_SAMPLE_RATE || 1.0,
});
