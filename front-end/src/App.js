// App.js
import React from 'react';
import Header from './components/Header';
import AppRoutes from './routes';
import 'bootstrap/dist/css/bootstrap.min.css';
import { CartProvider } from './components/CartContext';
import Footer from './components/Footer.js';
import Privacy from './components/PrivacyPolicy.js';
import AboutUs from './components/AboutUs.js';
import Terms from './components/TermsOfService.js';
import Contact from './components/Contact.js';
import ProductDetailPage from './components/ProductDetailPage.js';

function App() {
    return (
        <CartProvider>
            <Header />
            <AppRoutes />
        <Footer />
   
  {/*   <Footer />
    <Privacy />
    <AboutUs />
    <Terms /> */}

    </CartProvider>
    );
}

export default App;
