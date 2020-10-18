import configs from 'shell/configs';
import {showLoader, hideLoader} from 'shell/loader'
function getHeaders() {
  return new Headers({
    'Content-Type': 'application/json',
    'Authorization': 'basic'
  });
}

export function getServiceUrl(baseUrl, api) {
  return `http://${baseUrl}/${api}`
}

export async function get(url,  data, options) {
  const headers = getHeaders();
  const request = new Request(url, {
    credentials: 'include',
    method: 'GET',
    headers,
    body: JSON.stringify(data)
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

export async function deletion(url, data, options) {
  const headers = getHeaders();
  const request = new Request(url, {
    credentials: 'include',
    method: 'DELETE',
    headers,
    body: JSON.stringify(data)
  });
  return await fetch(request);
}

export default class BaseServices {
  getURL(api) {
    return getServiceUrl(configs[`${this.configsPrefix}-baseUrl`], api);
  }

  async get(api, params) {
    const url = this.getURL(api);
    showLoader();
    const res = await get(url, params);
    hideLoader();
    return res;
  }

  async post(api, data) {
    const url = this.getURL(api);
    showLoader();
    const res = await post(url, data);
    hideLoader();
    return res;
  }

  async put(api, data) {
    const url = this.getURL(api);
    showLoader();
    const res = await put(url, data);
    hideLoader();
    return res;
  }

  async delete(api, data) {
    const url = this.getURL(api);
    showLoader();
    const res = await deletion(url, data);
    hideLoader();
    return res;
  }
}
