import React from "react";

export const RepeatBlock = ({item, handleSpecialNavigate, messages}) => {
  return (
    <div className="repeat-block">
      <div>
        <p>{item.position}</p>
        <h5>{item.company}</h5>
      </div>
      <div className="nav-btn">
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
    </div>
  );
};
