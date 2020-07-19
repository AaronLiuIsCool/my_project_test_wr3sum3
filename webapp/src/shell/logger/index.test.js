import { getLogger } from './index';

describe("logger tests", () => {

  beforeEach(() => {
    global.console.info = jest.fn();
    global.console.log = jest.fn();
    global.console.warn = jest.fn();
    global.console.error = jest.fn();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  test('Logger works with a prefix name', () => {
    const logger = getLogger("Test");

    logger.info("this is a info");
    expect(global.console.info.mock.calls[0]).toEqual(['[Test]', 'this is a info']);

    logger.log("this is a log");
    expect(global.console.log).toHaveBeenCalledTimes(1);
    expect(global.console.log.mock.calls[0]).toEqual(['[Test]', 'this is a log']);

    logger.warn("this is a warn");
    expect(global.console.warn).toHaveBeenCalledTimes(1);
    expect(global.console.warn.mock.calls[0]).toEqual(['[Test]', 'this is a warn']);

    logger.error("this is a error");
    expect(global.console.error).toHaveBeenCalledTimes(1);
    expect(global.console.error.mock.calls[0]).toEqual(['[Test]', 'this is a error']);
  });
});

