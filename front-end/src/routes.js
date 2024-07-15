import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import SecuredPage from './components/SecuredPage'
import Home from './components/Home';
import PrivateRoute from './components/PrivateRoute';
import Cart from './components/Cart';

const AppRoutes = () => (
    
    <BrowserRouter>
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/secured" element={<PrivateRoute><SecuredPage /></PrivateRoute>} />
      <Route path="/cart" element={<Cart />} />
    </Routes>
    </BrowserRouter>

);

export default AppRoutes;
