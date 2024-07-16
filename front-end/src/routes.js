import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import SecuredPage from './components/SecuredPage'
import Home from './components/Home';
import PrivateRoute from './components/PrivateRoute';
import Cart from './components/Cart';
import AboutUs from './components/AboutUs';
import TermsOfService from './components/TermsOfService';
import PrivacyPolicy from './components/PrivacyPolicy';
import Contact from './components/Contact';
import ProductDetailPage from './components/ProductDetailPage';

const AppRoutes = () => (
    
    <BrowserRouter>
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/secured" element={<PrivateRoute><SecuredPage /></PrivateRoute>} />
      <Route path="/cart" element={<Cart />} />
      <Route path="/about" element={<AboutUs/>} />
      <Route path="/terms" element={<TermsOfService />} />
      <Route path="/privacy" element={<PrivacyPolicy />} />
      <Route path="/contact" element={<Contact />} />
      <Route path="productdetail" element={<ProductDetailPage />} />
    </Routes>
    </BrowserRouter>

);

export default AppRoutes;
