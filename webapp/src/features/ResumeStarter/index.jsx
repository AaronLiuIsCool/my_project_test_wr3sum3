import React, { Suspense } from 'react';
import LoadFallbackComponent from 'components/LoadFallbackComponent';

const ResumeStarter = React.lazy(() => import('./components/ResumeStarter'));

const LazyLoadResumeStarter = () => {

	return (
		<Suspense fallback={<LoadFallbackComponent />}>
				<ResumeStarter />
		</Suspense>
	);
};

export default LazyLoadResumeStarter;
