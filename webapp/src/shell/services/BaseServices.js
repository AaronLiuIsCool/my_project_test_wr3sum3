import configs from 'shell/configs';

export function getServiceUrl(baseUrl, api) {
  return `http://${baseUrl}/${api}`
}

export async function get(url,  options) {
  const headers = new Headers();
  const request = new Request(url, {
    method: 'GET',
    headers,
    mode: 'cors',
    cache: 'default'
  });
  return await fetch(request);
}

export async function post(url, data, options) {
  const headers = new Headers();
  const request = new Request(url, {
    method: 'POST',
    headers,
    body: JSON.stringify(data),
    mode: 'cors',
    cache: 'default'
  });
  return await fetch(request);
}

export default class BaseServices {
  getURL(api) {
    return getServiceUrl(configs[`${this.configsPrefix}-baseUrl`], api);
  }

  async get(api) {
    const url = this.getURL(api);
    return await get(url);
  }

  async post(api, data) {
    const url = this.getURL(api);
    return await post(url, data);
  }
}
