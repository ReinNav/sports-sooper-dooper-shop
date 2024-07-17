import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Cart.css';

const OrderFail = ({ orderid }) => {
    const navigate = useNavigate();

    const handleGoToCart = () => {
        navigate('/cart');
    };

    const handleBackToShopping = () => {
        navigate('/');
    };

    return (
        <div className='main-container vw-100 vh-100 d-flex justify-content-center align-items-center OrderFail'>
            <div className='row vw-100'>
                <div className='flex-column text-sm-center col-md w-50 orderFail'>
                    <h1 className='text-sm-center text-capitalize'>Ihre Bestellung konnte nicht aufgegeben werden.</h1>
                    <h1 className='text-sm-center text-capitalize'>Bestellnummer: {orderid}</h1>
                    <br />
                    <p>Es gab ein Problem bei der Bearbeitung Ihrer Bestellung. Bitte versuchen Sie es erneut.</p>
                    <div className='p-4 justify-content-center'>
                        <button className='btn-dark btn' onClick={handleGoToCart}>Zum Warenkorb</button>
                        <button className='btn-dark btn' onClick={handleBackToShopping}>Zurück zum Shop</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default OrderFail;