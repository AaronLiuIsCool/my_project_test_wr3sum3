import React from 'react';
import { Provider } from 'react-redux';
import renderer from 'react-test-renderer';

import store from 'store';
import { I8nContext } from 'shell/i18n';

import Jobs from './index';
import * as time from '../../utils/time';

const JobsWithProvider = (props) => (
    <Provider store={store}>
        <I8nContext.Provider value={{
            'job_list_summary': '{0}'
        }}>
            <Jobs {...props} />
        </I8nContext.Provider>
    </Provider>
);

const mockData = {
    "jobs":[
        {
            "jobUuid":"4dd748d4-0b22-11eb-9fa5-a56d63ee61ee",
            "title":"Software Development Engineer",
            "companyName":"微软",
            "location":{
                "country":"中国",
                "state":"",
                "city":"北京"
            },
            "jobType":"Full-time",
            "relevantMajors":[
                "Computer Science"
            ],
            "url":"https://cn.linkedin.com/jobs/view/software-development-engineer-at-%E5%BE%AE%E8%BD%AF-1998181365?utm_campaign=google_jobs_apply&utm_source=google_jobs_apply&utm_medium=organic",
            "postDate":"2020-10-04T17:59:12.768"
        },
        {
            "jobUuid":"7a8a44c2-1180-11eb-8f88-916881325b13",
            "title":"Software Engineer (University Hiring)",
            "companyName":"HPE",
            "location":{
                "country":"中国",
                "state":"Beijing",
                "city":"北京"
            },
            "url":"https://cn.jobrapido.com/jobpreview/96839405?utm_campaign=google_jobs_apply&utm_source=google_jobs_apply&utm_medium=organic",
            "postDate":"2020-10-13T20:28:27.43"
        },
        {
            "jobUuid":"4e67ef9a-0b22-11eb-9fa5-1979a4f02776",
            "title":"Analog Design Engineer",
            "companyName":"安森美半导体",
            "location":{
                "country":"中国",
                "state":"",
                "city":"北京"
            },
            "url":"https://cn.linkedin.com/jobs/view/analog-design-engineer-at-%E5%AE%89%E6%A3%AE%E7%BE%8E%E5%8D%8A%E5%AF%BC%E4%BD%93-2020144244?utm_campaign=google_jobs_apply&utm_source=google_jobs_apply&utm_medium=organic",
            "postDate":"2020-10-05T17:59:13.716"
        },
        {
            "jobUuid":"31c40d0a-1180-11eb-8f88-398cde6016b2",
            "title":"AI/ML - Software Engineer Intern, Machine Intelligence",
            "companyName":"Apple Inc.",
            "location":{
                "country":"中国",
                "state":"Beijing",
                "city":"北京"
            },
            "url":"https://cn.jobrapido.com/jobpreview/96834204?utm_campaign=google_jobs_apply&utm_source=google_jobs_apply&utm_medium=organic",
            "postDate":"2020-10-14T20:26:25.335"
        }
    ],
    "offset":0,
    "limit":7,
    "total":42
};

describe('Jobs', () => {
    beforeEach(() => {
        time.timeSince = jest.fn(() => '3 days ago');
    });

    test('render Jobs', async () => {
        const searchHeader = renderer.create(
            <JobsWithProvider data={{}} resumeId={'test id'} />
        );
        expect(searchHeader.toJSON()).toMatchSnapshot();
    });

    test('render Jobs with full data', async () => {
        const searchHeader = renderer.create(
            <JobsWithProvider data={mockData} resumeId={'test id'} />
        );
        expect(searchHeader.toJSON()).toMatchSnapshot();
    });
});
