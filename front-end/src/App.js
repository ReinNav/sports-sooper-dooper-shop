import React from 'react';
import Header from './components/Header';
import AppRoutes from './routes';
import 'bootstrap/dist/css/bootstrap.min.css';
import { CartProvider } from './components/CartContext';
import Footer from './components/Footer.js';

function App() {
    return (
        <CartProvider>
            <AppRoutes />
            <Footer />
        </CartProvider>
    );
}

export default App;
