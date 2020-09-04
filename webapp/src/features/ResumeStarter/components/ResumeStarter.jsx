import React from 'react';
import Button from 'react-bootstrap/Button';

// import { selectLanguage, selectUserId } from 'features/App/slicer';

import '../styles/index.scss';

function createNewResumeWithData() {
    //TODO: add upload resume functionality here
    alert('TODO: add upload resume functionality here');
}

const New = () => {
    return (
        <div className='features resume-starter m-auto'>
            <Button href='/resume'>
                创建一份全新简历
            </Button>
            <Button onClick={createNewResumeWithData}>
                我有简历了，我要上传
            </Button>
        </div>
    );
};

export default New;