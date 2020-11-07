import BaseServices from './BaseServices';

class TestServices extends BaseServices {
  constructor() {
    super();
    this.configsPrefix = 'test-baseservices';
  }
}

describe('BaseServices tests', () => {
  let testServices;

  beforeEach(() => {
    global.fetch = jest.fn().mockResolvedValue({json: () => Promise.resolve()});
    testServices = new TestServices();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  test("it should get the correct url", () => {
    expect(testServices.getURL("testApi")).toBe('http://test.kuaidao.com/testApi');
  });

  test("it should call fetch get", async () => {
    await testServices.get("testApi");
    expect(global.fetch).toHaveBeenCalledTimes(1);
  });

  test("it should call fetch post", async () => {
    await testServices.post("testApi");
    expect(global.fetch).toHaveBeenCalledTimes(1);
  });
})
