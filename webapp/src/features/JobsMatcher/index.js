import React, { Suspense } from 'react';

import LoadFallbackComponent from 'components/LoadFallbackComponent';
import './styles/index.scss';

const JobMatcher = React.lazy(() => import('./components/JobsMatcher'));

const LazyLoadJobMatcher = ({ location }) => {
  const query = new URLSearchParams(location.search);
  return (
    <Suspense fallback={<LoadFallbackComponent />}>
      <JobMatcher resume={query.get('resume')}/>
    </Suspense>
  );
};

export default LazyLoadJobMatcher;
