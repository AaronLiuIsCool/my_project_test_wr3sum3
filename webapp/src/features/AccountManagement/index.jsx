import React, { Suspense } from 'react';

import LoadFallbackComponent from 'components/LoadFallbackComponent';

const AccountManagement = React.lazy(() => import('./components/AccountManagement'));

const LazyLoadAccountManagement = () => (
  <Suspense fallback={<LoadFallbackComponent />}>
    <AccountManagement />
  </Suspense>
);

export default LazyLoadAccountManagement;
