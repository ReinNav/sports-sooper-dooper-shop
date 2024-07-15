// App.js
import React from 'react';
import Header from './components/Header';
import AppRoutes from './routes';
import 'bootstrap/dist/css/bootstrap.min.css';
import { CartProvider } from './components/CartContext';

function App() {
    return (
        <CartProvider>
            <Header />
            <AppRoutes />
        </CartProvider>
    );
}

export default App;
