import React from 'react';

import SmartResume from 'features/SmartResume';
import Counter from 'features/CounterExample';

import KButton from 'components/KButton';

import './App.css';

function App() {
  return (
    <div className="App">
      <SmartResume />

      <br />
      <br />
      <br />
      <p>Below is the example Bootstrap Component Wrapper</p>
      <KButton variant="primary">Primary</KButton>{' '}
      <KButton variant="secondary">Secondary</KButton>{' '}
      <KButton variant="success">Success</KButton>{' '}
      <KButton variant="warning">Warning</KButton>{' '}
      <KButton variant="danger">Danger</KButton>
      <KButton variant="info">Info</KButton>{' '}
      <KButton variant="light">Light</KButton>
      <KButton variant="dark">Dark</KButton>{' '}
      <KButton variant="link">Link</KButton>

      <br />
      <br />
      <br />
      <p>Below is the example code with Redux and Hooks</p>
      <Counter />
    </div>
  );
}

export default App;
