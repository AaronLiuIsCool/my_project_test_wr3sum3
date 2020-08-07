import React, { Suspense } from 'react';

import LoadFallbackComponent from 'components/LoadFallbackComponent';
import './styles/index.scss';

const JobMatcher = React.lazy(() => import('./components/JobsMatcher'));

const LazyLoadJobMatcher = () => (
  <Suspense fallback={<LoadFallbackComponent />}>
    <JobMatcher />
  </Suspense>
);

export default LazyLoadJobMatcher;
