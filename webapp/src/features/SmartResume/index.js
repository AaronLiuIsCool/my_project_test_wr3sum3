import React, { Suspense } from 'react';

import LoadFallbackComponent from 'components/LoadFallbackComponent';
import './styles/index.css';

const SmartResume = React.lazy(() => import('./components/SmartResume'));

const LazyLoadSmartResume = () => (
  <Suspense fallback={<LoadFallbackComponent />}>
    <SmartResume />
  </Suspense>
);

export default LazyLoadSmartResume;
