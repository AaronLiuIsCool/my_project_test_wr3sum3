import React, { Suspense } from 'react';

import LoadFallbackComponent from 'components/LoadFallbackComponent';
import './styles/index.scss';

const ResumeStarter = React.lazy(() => import('./components/ResumeStarter'));

const LazyLoadResumeStarter = () => (
  <Suspense fallback={<LoadFallbackComponent />}>
    <ResumeStarter />
  </Suspense>
);

export default LazyLoadResumeStarter;
