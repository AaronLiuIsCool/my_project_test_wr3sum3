import React, { Suspense } from 'react';

import LoadFallbackComponent from 'components/LoadFallbackComponent';
import './styles/index.scss';

const ResumeHub = React.lazy(() => import('./components/ResumeHub'));

const LazyLoadResumeHub = () => (
  <Suspense fallback={<LoadFallbackComponent />}>
    <ResumeHub />
  </Suspense>
);

export default LazyLoadResumeHub;
