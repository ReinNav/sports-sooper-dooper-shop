import React from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import '../styles/Cart.css';

const OrderConfirmation = () => {
    const { orderid } = useParams();
    const navigate = useNavigate();

    const handleBackToShopping = () => {
        navigate('/');
    };

    return (
        <div className='main-container vw-100 vh-100 d-flex justify-content-center align-items-center OrderConfirmation'>
            <div className='row vw-100'>
                <div className='flex-column text-sm-center col-md w-50 orderConfirmation'>
                    <h1 className='text-sm-center text-capitalize'>Ihre Bestellung wurde aufgegeben.</h1>
                    <h1 className='text-sm-center text-capitalize'>Bestellnummer: {orderid}</h1>
                    <br />
                    <p>Vielen Dank für Ihre Bestellung. Bitte überprüfen Sie Ihr E-Mail-Konto auf die Bestätigungsmail.</p>
                    <div className='p-4 justify-content-center'>
                        <button className='btn-dark btn' onClick={handleBackToShopping}>Zurück zum Shop</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default OrderConfirmation;
