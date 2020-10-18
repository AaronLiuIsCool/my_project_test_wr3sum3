export const showLoader = () => {
  if(document.getElementById('root')) {
    document.getElementById('root').classList.add('loading');
  }
};

export const hideLoader = () => {
  if(document.getElementById('root')) {
    document.getElementById('root').classList.remove('loading');
  }
};