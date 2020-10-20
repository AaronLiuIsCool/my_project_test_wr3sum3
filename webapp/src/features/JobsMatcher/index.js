import React, { Suspense } from 'react';
import { Redirect } from 'react-router-dom';

import LoadFallbackComponent from 'components/LoadFallbackComponent';
import './styles/index.scss';

const JobMatcher = React.lazy(() => import('./components/JobsMatcher'));

const LazyLoadJobMatcher = ({ location }) => {
  const query = new URLSearchParams(location.search);
  const resumeId = query.get('resume');
  const page = query.get('page');

  if (!resumeId) {
    return <Redirect to='/' />;
  }

  return (
    <Suspense fallback={<LoadFallbackComponent />}>
      <JobMatcher resume={resumeId} page={page} />
    </Suspense>
  );
};

export default LazyLoadJobMatcher;
