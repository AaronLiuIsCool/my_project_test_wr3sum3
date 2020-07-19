import React from 'react';

import { useI8n } from 'shell/i18n';

const LeftNav = () => {
    const messages = useI8n();
    return (
        <div>
            TODO: Navigation on the left
            <p>{messages.basicInfo}</p>
            <p>{messages.education}</p>
            <p>{messages.workXp}</p>
            <p>{messages.projectXp}</p>
            <p>{messages.otherXp}</p>
            <p>{messages.certifications}</p>
        </div>
    );
}

export default LeftNav;