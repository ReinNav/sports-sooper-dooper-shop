import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import SecuredPage from './components/SecuredPage'
import Home from './components/Home';
import PrivateRoute from './components/PrivateRoute';

const AppRoutes = () => (
    
    <BrowserRouter>
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/secured" element={<PrivateRoute><SecuredPage /></PrivateRoute>} />
    </Routes>
    </BrowserRouter>

);

export default AppRoutes;
