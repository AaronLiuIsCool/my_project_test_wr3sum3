import React, { useEffect } from 'react';
import './RatingDetail.scss';
import Dropdown from './Dropdown';

import { useI8n } from 'shell/i18n';

// TODO: 改成多语言
const options = ['全部', '信息完整度', '专业表达度', '版式', '经历含金量'];

const RatingDetail = () => {
    const messages = useI8n();
    const BLOCKS = [
        {
            title: messages.basicInfo
        },
        {
            title: messages.education
        },
        {
            title: messages.workXp
        },
        {
            title: messages.projectXp
        },
        {
            title: messages.otherXp
        },
        {
            title: messages.certifications
        }
    ];

    return (
        <div className="rating-detail">
            <div className="fading-container">
                <div className="sticky-section">
                    <h3>简历强度分析详情</h3>
                    <Dropdown
                        options={options}
                        onSelect={() => {
                            console.log(123);
                        }}
                    />
                </div>
                <div className="content-section">
                    {BLOCKS.map((block) => {
                        return (
                            <div key={block.title} className="blocks">
                                <h3>
                                    {block.title} <span>(1)</span>
                                </h3>
                                <div className="action-item warning">
                                    <div className="action-details">
                                        <h4>
                                            您没有填写<span>联系电话</span>
                                        </h4>
                                        <p>没有填写电话别人会找不到你</p>
                                        <div className="tag">
                                            <span>信息完整度得分</span>
                                            <span className="tag-arrow"></span>
                                        </div>
                                    </div>
                                    <div className="nav-btn">
                                        <button>前往</button>
                                    </div>
                                </div>
                                <div className="action-item pass">
                                    <div className="action-details">
                                        <h4>学校知名度非常好！</h4>
                                        <p>
                                            您的硕士毕业院校是清华大学。清华大学是中国985知名高校，世界排名前100。
                                        </p>
                                        <div className="tag">
                                            <span>经历含金量</span>
                                            <span className="tag-arrow"></span>
                                        </div>
                                    </div>
                                    <div className="nav-btn">
                                        <button>前往</button>
                                    </div>
                                </div>
                            </div>
                        );
                    })}
                </div>
            </div>
        </div>
    );
};

export default RatingDetail;
