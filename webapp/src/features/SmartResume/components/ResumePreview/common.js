import React from "react";

import styles from "../../styles/ResumePreview.module.css";
// import { useI8n } from 'shell/i18n';

// const messages = useI8n.t;
export const dateRangeBuilder = (start, end) => {
  let res;
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

export const render_Description = (description, lineStart, pageBreakCount) => {
  if (description) {
    const descriptionList = description.split("\n");
    return descriptionList.map((content, index) => {
      return (
        <React.Fragment key={index}>
          <div className={styles.infoGroupItem}>{content}</div>
          {render_pageBreaker(++lineStart, pageBreakCount)}
        </React.Fragment>
      );
    });
  }
};

const PageBreaker = () => <div className={styles.pageBreaker}></div>;
// update line counter + insert page breaker if needed
// pageBreakCount - Up to which number we need to break the page, this number is decided by fontsize, line height and other CSS ...
export const render_pageBreaker = (lineCount, pageBreakCount) => {
  if (lineCount >= pageBreakCount && lineCount % pageBreakCount === 0) {
    return <PageBreaker />;
  }
  return "";
};
