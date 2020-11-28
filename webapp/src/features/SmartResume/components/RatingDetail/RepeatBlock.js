import React from "react";

export const RepeatBlock = ({positive = false, feedback, item, handleSpecialNavigate, messages}) => {
  return (
    <div className="repeat-block">
      <div>
        <p>{item.position}</p>
        <h5>{item.company}</h5>
        {positive && <p className="full-width">
          <span style={{fontWeight: 'bold'}}>{messages.keyword}</span>
          {feedback.map((e, index) => <span key={index} className="keywords">{e}</span>)}
        </p>}
      </div>
      {
        !positive && <div className="nav-btn">
        <button
          data-obj={item.quantify}
          data-section={item.section}
          onClick={() => {
            handleSpecialNavigate(item.section, item);
          }}
        >
          {messages.jumpTo}
        </button>
      </div>
      }
    </div>
  );
};
