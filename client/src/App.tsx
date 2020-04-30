import React from 'react';
import { Admin } from './Admin';
import logo from './logo.svg';
import './App.css';
import { BrowserRouter, Switch } from 'react-router-dom';

import axios from 'axios';

const TIMEOUT = 60 * 1000;
axios.defaults.timeout = TIMEOUT;
axios.defaults.baseURL = 'http://localhost:8080';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Admin />
      </BrowserRouter>
    </div>
  );
}

export default App;
