export function timeSince(dateStr) {
  const date = new Date(dateStr);
  const seconds = Math.floor((new Date() - date) / 1000);
  let interval = seconds / 31536000;

  if (interval > 1) {
      return `${Math.floor(interval)}年前发布`;
  }
  interval = seconds / 2592000;
  if (interval > 1) {
      return `${Math.floor(interval)}月前发布`;
  }
  interval = seconds / 86400;
  if (interval > 1) {
      return `${Math.floor(interval)}天前发布`;
  }
  interval = seconds / 3600;
  if (interval > 1) {
      return `${Math.floor(interval)}小时前发布`;
  }
  interval = seconds / 60;
  if (interval > 1) {
      return `${Math.floor(interval)}分钟前发布`;
  }
  return `${Math.floor(interval)}秒前发布`;
}
