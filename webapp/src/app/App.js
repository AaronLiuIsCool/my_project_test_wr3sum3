import React from 'react';

import SmartResume from 'features/SmartResume';
import Counter from 'features/CounterExample';

import './App.css';

function App() {
  return (
    <div className="App">
      <SmartResume />
      <div>
        <br />
        <br />
        <br />
        Below is the example code
      </div>
      <Counter />
    </div>
  );
}

export default App;
