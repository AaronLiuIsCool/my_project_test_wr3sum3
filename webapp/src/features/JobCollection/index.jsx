import React, { Suspense } from 'react';

import LoadFallbackComponent from 'components/LoadFallbackComponent';

const JobCollection = React.lazy(() => import('./components/JobCollection'));

const LazyLoadJobCollection = () => (
  <Suspense fallback={<LoadFallbackComponent />}>
    <JobCollection />
  </Suspense>
);

export default LazyLoadJobCollection;
