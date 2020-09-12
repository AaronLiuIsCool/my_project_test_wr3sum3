// const messages = useI8n.t;
export const dateRangeBuilder = (start, end) => {
  let res = "";
  if (start) {
    res = start.slice(0, 7);
    if (end.slice(0, 7)) {
      res += " - " + end.slice(0, 7);
    }
  } else if (end) {
    res = end.slice(0, 7);
  }
  return res;
};
