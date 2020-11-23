// const messages = useI8n.t;
export const dateRangeBuilder = (start, end, messages) => {
  let res = "";

  if (start && end) {
    res = `${start.slice(0, 7).replace("-", "/")} - ${end.slice(0, 7).replace("-", "/")}`;
  } else if (!start) {
    return "";
  }
  else if (!end) {
    res = `${start.slice(0, 7).replace("-", "/")} - ${messages.current}`;
  }
  return res;
};
