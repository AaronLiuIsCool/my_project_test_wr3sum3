import React from 'react';
import styles from '../../styles/ResumePreview.module.css';
// import { useI8n } from 'shell/i18n';

// const messages = useI8n.t;
export const dateRangeBuilder = (start, end) =>{
  let res;
  if (start){
      res = start.slice(0, 7);
      if (end.slice(0, 7)){
          res += " - " + end.slice(0, 7)
      }
      
  }
  else if (end){
      res = end.slice(0, 7);
  }
  return res;
}


export const render_Description = (description, lineStart) => {
    if (description) {
        const descriptionList = description.split('\n');
        return descriptionList.map((content, index) => {
            return (
                <React.Fragment key={index}>
                    <div className={styles.infoGroupItem}>
                        {content}
                    </div>
                    {render_pageBreaker(++lineStart)}
                </React.Fragment>
            );
        });
    }
};

const PageBreaker = () => <div className={styles.pageBreaker} ></div>;

// update line counter + insert page breaker if needed
export const render_pageBreaker = (lineCount) => {
    if ((lineCount) % 25 === 0){
        return  <PageBreaker/>;
    }
    return "";
}
