import * as Sentry from "@sentry/browser";

export function getLogger(name, metadata) {
  const context = `[${name}]`;
  const scope = new Sentry.Scope();
  scope.setContext('logger', {
    name, ...metadata,
  });
  return {
    info: function () {
      return Function.prototype.bind.call(console.info, console, context);
    }(),
    log: function () {
      return Function.prototype.bind.call(console.log, console, context);
    }(),
    warn: function () {
      return Function.prototype.bind.call(console.warn, console, context);
    }(),
    error: function (err) {
      return Sentry.captureException(err, scope);
    }
  };
}
