import React from 'react';

import Button from 'react-bootstrap/Button';

import '../styles/index.scss';

const New = () => {
    // TODO: Move the resume creation part in SmartResume to here
    return (
        <div className='features resume-starter m-auto'>
            <Button href='/resume'>创建一份全新简历</Button>
            <Button>我有简历了，我要上传</Button>
        </div>
    );
};

export default New;