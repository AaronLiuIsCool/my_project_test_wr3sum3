import React, { Suspense } from 'react';

import LoadFallbackComponent from 'components/LoadFallbackComponent';

const JobRefinementDetails = React.lazy(() => import('./components/JobRefinement'));

const LazyLoadJobRefinement = () => (
  <Suspense fallback={<LoadFallbackComponent />}>
    <JobRefinementDetails />
  </Suspense>
);

export default LazyLoadJobRefinement;
