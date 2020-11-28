import React from 'react';
import {RepeatBlock} from './RepeatBlock';

const renderFeedback = (good = '', bad = '') => {
  return (
    <>
      {good}
      {bad}
    </>
  );
};
const renderKeywordsNegativeFeedback = (
  source,
  handleSpecialNavigate,
  type,
  messages,
) => {
  const aspect = source.filter((item) => {
    return !item.feedback;
  });
  if (aspect.length < 1) return '';
  return (
    <div className="action-item warning">
      <div className="action-details">
        <h4>
          {messages.expLackOf}
          <span>{messages.keywords}</span>
        </h4>
        <p className="full-width">
          {messages.keywordsMessage.replace(
            '{exp}',
            messages.expReplacement[type],
          )}
        </p>
        {aspect.map((item, index) => (
          <RepeatBlock
            messages={messages}
            key={index}
            item={item}
            handleSpecialNavigate={handleSpecialNavigate}
          />
        ))}

        <div className="tag">
          <span>{messages.expression}</span>
          <span className="tag-arrow"></span>
        </div>
      </div>
    </div>
  );
};
const renderKeywordsPositiveFeedback = (source, type, messages) => {
  const aspect = source.filter((item) => {
    return !!item.feedback;
  });
  if (aspect.length < 1) return '';
  return (
    <div className="action-item pass">
      <div className="action-details">
        <h4>{messages.keywordsMetHeader}</h4>
        <p className="full-width">{messages.keywordsMetMessage}</p>
        {aspect.map((item, index) => (
          <RepeatBlock
            positive={true}
            messages={messages}
            key={index}
            item={item}
            feedback={item.feedback}
          />
        ))}

        <div className="tag">
          <span>{messages.expression}</span>
          <span className="tag-arrow"></span>
        </div>
      </div>
    </div>
  );
};
const renderQuantifyNegativeFeedback = (
  source,
  handleSpecialNavigate,
  type,
  messages,
) => {
  const aspect = source.filter((item) => {
    return !item.feedback;
  });
  if (aspect.length < 1) return '';
  return (
    <div className="action-item warning">
      <div className="action-details">
        <h4>
          {messages.exp} <span>{messages.notQuantify}</span>
        </h4>
        <p className="full-width">
          {messages.quantifyMessage.replace(
            '{exp}',
            messages.expReplacement[type],
          )}
        </p>
        {aspect.map((item, index) => (
          <RepeatBlock
            messages={messages}
            key={index}
            item={item}
            handleSpecialNavigate={handleSpecialNavigate}
          />
        ))}

        <div className="tag">
          <span>{messages.expression}</span>
          <span className="tag-arrow"></span>
        </div>
      </div>
    </div>
  );
};
const renderQuantifyPositiveFeedback = (source, type, messages) => {
  const aspect = source.filter((item) => {
    return !!item.feedback;
  });
  if (aspect.length < 1) return '';
  return (
    <div className="action-item pass">
      <div className="action-details">
        <h4>{messages.quantifyMetHeader}</h4>
        <p className="full-width">{messages.quantifyMetMessage}</p>
        {aspect.map((item, index) => (
          <RepeatBlock
            positive={true}
            messages={messages}
            key={index}
            item={item}
            feedback={item.feedback}
          />
        ))}

        <div className="tag">
          <span>{messages.expression}</span>
          <span className="tag-arrow"></span>
        </div>
      </div>
    </div>
  );
};

export const ExpBlocks = ({
  info,
  handleSpecialNavigate,
  type,
  jumpTo,
  messages,
}) => {
  const calculateTotal = (...args) => {
    return args.reduce((acc, cur) => acc + cur, 0);
  };
  const labelMappings = {
    'info-complete': messages.infoComplete,
    layout: messages.layout,
    'exp-value': messages.expValue,
  };

  const getTitle = (type) => {
    switch (type) {
      case 'workXp':
        return messages.workTitle;
      case 'projectXp':
        return messages.projTitle;
      case 'otherXp':
        return messages.volTitle;
      default:
        break;
    }
  };

  return (
    <>
      {calculateTotal(
        info.company.length,
        info.keywords.length,
        info.quantify.length,
        info.amount.length,
        info.sorted.length,
      ) > 0 && (
        <div className="blocks">
          <h3>
            {getTitle(type)}{' '}
            <span>
              (
              {calculateTotal(
                info.company.length,
                info.keywords.length,
                info.quantify.length,
                info.amount.length,
                info.sorted.length,
              )}
              )
            </span>
          </h3>

          {/* 专业术语 */}
          {info.keywords.length > 0 &&
            renderFeedback(
              renderKeywordsPositiveFeedback(info.keywords, type, messages),
              renderKeywordsNegativeFeedback(
                info.keywords,
                handleSpecialNavigate,
                type,
                messages,
              ),
            )}
          {/* 数量术语 */}
          {info.quantify.length > 0 &&
            renderFeedback(
              renderQuantifyPositiveFeedback(info.quantify, type, messages),
              renderQuantifyNegativeFeedback(
                info.quantify,
                handleSpecialNavigate,
                type,
                messages,
              ),
            )}
          {/* 公司质量 */}
          {info.company.length > 0 &&
            info.company.map((item, index) => (
              <div key={index} className="action-item pass">
                <div className="action-details">
                  <h4>{item.name}</h4>
                  <p>{item.message}</p>
                  <div className="tag">
                    <span>{labelMappings[item.type]}</span>
                    <span className="tag-arrow"></span>
                  </div>
                </div>
                <div className="nav-btn"></div>
              </div>
            ))}

          {/* 经历数量 */}
          {info.amount.map((item, index) => (
            <div
              key={index}
              className={
                item.green ? 'action-item pass' : 'action-item warning'
              }
            >
              <div className="action-details">
                <h4>{item.name}</h4>
                <p>{item.message}</p>
                <div className="tag">
                  <span>{labelMappings[item.type]}</span>
                  <span className="tag-arrow"></span>
                </div>
              </div>
              <div className="nav-btn"></div>
            </div>
          ))}

          {/* 是否排序正确 */}
          {info.sorted.length > 0 &&
            info.sorted.map((item, index) => (
              <div key={index} className="action-item warning">
                <div className="action-details">
                  <h4>
                    {item.name}
                    <span>{messages.hasProblem}</span>
                  </h4>
                  <p>{item.message}</p>
                  <div className="tag">
                    <span>{labelMappings[item.type]}</span>
                    <span className="tag-arrow"></span>
                  </div>
                </div>
                <div className="nav-btn">
                  <button
                    onClick={() => {
                      jumpTo(item.section);
                    }}
                  >
                    {messages.sort}
                  </button>
                </div>
              </div>
            ))}
        </div>
      )}
    </>
  );
};
