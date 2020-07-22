import configs from 'shell/configs';

describe("configurations test", () => {
  test("configs got override correctly", () => {
    expect(configs.env).toBe('test');
    expect(configs["test-baseservices-baseUrl"]).toBe('test.kuaidao.com');
  });
});
