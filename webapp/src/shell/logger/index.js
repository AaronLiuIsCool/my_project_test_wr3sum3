export function getLogger(name) {
  const context = `[${name}]`;
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
    error: function () {
      return Function.prototype.bind.call(console.error, console, context);
    }()
  };
}
