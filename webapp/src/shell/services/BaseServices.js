import configs from 'shell/configs';

function getHeaders() {
  return new Headers({
    'Content-Type': 'application/json',
    'Authorization': 'basic'
  });
}

export function getServiceUrl(baseUrl, api) {
  return `http://${baseUrl}/${api}`
}

export async function get(url,  options) {
  const headers = getHeaders();
  const request = new Request(url, {
    credentials: 'include',
    method: 'GET',
    headers
  });
  return await fetch(request);
}

export async function post(url, data, options) {
  const headers = getHeaders();
  const request = new Request(url, {
    credentials: 'include',
    method: 'POST',
    headers,
    body: JSON.stringify(data)
  });
  return await fetch(request);
}

export async function put(url, data, options) {
  const headers = getHeaders();
  const request = new Request(url, {
    credentials: 'include',
    method: 'PUT',
    headers,
    body: JSON.stringify(data)
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

  async put(api, data) {
    const url = this.getURL(api);
    return await put(url, data);
  }
}
