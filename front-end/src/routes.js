import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Home from './components/Home';
import Cart from './components/Cart';
import ProductListing from "./components/ProductListing";
import AboutUs from './components/AboutUs';
import TermsOfService from './components/TermsOfService';
import PrivacyPolicy from './components/PrivacyPolicy';
import Contact from './components/Contact';
import ProductDetailPage from './components/ProductDetailPage';
import Checkout from './components/Checkout';
import CapturePayment from './components/CapturePayment';
import OrderConfirmation from "./components/OrderConfirmation";
import OrderFail from "./components/OrderFail";

const AppRoutes = () => (
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/success/:orderid" element={<OrderConfirmation />} />
            <Route path="/failure/:orderid" element={<OrderFail />} />
            <Route path="/cart" element={<Cart />} />
        <Route path="/products" element={<ProductListing />} />
            <Route path="/about" element={<AboutUs />} />
            <Route path="/terms" element={<TermsOfService />} />
            <Route path="/privacy" element={<PrivacyPolicy />} />
            <Route path="/contact" element={<Contact />} />
            <Route path="/product/:id" element={<ProductDetailPage />} />
            <Route path="/checkout" element={<Checkout />} />
            <Route path="/capture" element={<CapturePayment />} />
        </Routes>
    </BrowserRouter>
);

export default AppRoutes;
