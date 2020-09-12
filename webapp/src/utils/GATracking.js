import ReactGA from 'react-ga';

/**
 * GAInitialize - call this initialize function before app runs
 */
export function GAInitialize() {
  ReactGA.initialize('UA-177419863-1');
}

/**
 * GApageView -  To report page view
 */
export const GApageView = () => {
  ReactGA.pageview(window.location.pathname +  
    window.location.search);
}


/**
 * GAEvent - Add custom tracking event.
 * @param {string} category 
 * @param {string} action 
 */
export const GAEvent = (category, action) => {
  ReactGA.event({
    category: category,
    action: action,
  });
};