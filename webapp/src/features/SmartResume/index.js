import React, { Suspense } from 'react';

import LoadFallbackComponent from 'components/LoadFallbackComponent';
import './styles/index.scss';

const SmartResume = React.lazy(() => import('./components/SmartResume'));

const LazyLoadSmartResume = () => (
  <Suspense fallback={<LoadFallbackComponent />}>
    <SmartResume useObserver={true} />
  </Suspense>
);

export default LazyLoadSmartResume;
