import React, { useEffect, useCallback } from 'react';
import {useNavigate, useParams} from 'react-router-dom'; // Import useNavigate
import '../Cart.css';



const OrderConfirmation = () => {
    const {orderid} = useParams();
    const navigate = useNavigate(); // Initialize useNavigate

    const handleBackToShopping = () => {
        navigate('/');
    };

    return (
        <div class='vw-100 p-4' className={'OrderConfirmation'}>
            <div class='row vw-100 '>

                <div class='text-sm-center col-md w-50' className="orderConfirmation">
                    <h1 class='text-sm-center text-capitalize'>Your Order has been placed. Order Number: {orderid}</h1>
                    <br></br>
                    <text>Thank you for your order. Please check your E-Mail account for the order confirmation e-mail.
                    </text>
                    <div class='p-4 justify-content-center'>
                        <button class='btn-dark btn ' onClick={handleBackToShopping}>Back to shopping
                        </button>
                    </div>
                </div>


            </div>
        </div>
    );
};

export default OrderConfirmation;
