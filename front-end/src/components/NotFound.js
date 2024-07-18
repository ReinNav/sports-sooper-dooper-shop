import React from 'react';
import { useNavigate } from 'react-router-dom';

const OrderConfirmation = () => {
    const navigate = useNavigate();

    const handleBackToShopping = () => {
        navigate('/');
    };

    return (
        <div className='main-container vw-100 vh-100 d-flex justify-content-center align-items-center'>
            <div className='row vw-100'>
                <div className='flex-column text-sm-center col-md w-50'>
                    <h1 className='text-sm-center'>Seite nicht gefunden.</h1>
                    <br />
                    <div className='p-4 justify-content-center'>
                        <button className='btn-dark btn' onClick={handleBackToShopping}>Zurück zum Shop</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default OrderConfirmation;
