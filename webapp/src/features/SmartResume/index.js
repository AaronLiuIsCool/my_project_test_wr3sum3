import React, { Suspense } from 'react';

import LoadFallbackComponent from 'components/LoadFallbackComponent';
import './styles/index.scss';

const SmartResume = React.lazy(() => import('./components/SmartResume'));

const LazyLoadSmartResume = ({ match }) => (
  <Suspense fallback={<LoadFallbackComponent />}>
    <SmartResume useObserver={true} resumeId={match.params.resumeId} />
  </Suspense>
);

export default LazyLoadSmartResume;
