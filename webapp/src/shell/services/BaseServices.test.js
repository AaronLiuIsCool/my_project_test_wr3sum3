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
    jest.spyOn(global, 'fetch');
    testServices = new TestServices();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  test("it should get the correct url", () => {
    expect(testServices.getURL("testApi")).toBe('http://test.kuaidao.com/testApi');
  });
})
