import React from 'react';

import LeftNav from './components/LeftNav';
import ExperiencesForm from './components/ExperiencesForm';
import Preview from './components/Preview';

const SmartResume = () => (
    <div>
        <div>智能创建</div>
        <LeftNav />
        <ExperiencesForm />
        <Preview />
    </div>
);

export default SmartResume;