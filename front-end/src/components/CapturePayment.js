import React, { useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { completePayment } from './Api/PaymentApi';

const CapturePayment = () => {
    const navigate = useNavigate();
    const location = useLocation();
    useEffect(() => {
        const queryParams = new URLSearchParams(location.search);
        const token = queryParams.get('token');
        if (token) {
            handlePaymentCompletion(token);
        } else {
            console.error('No token found');
        }
    }, [location]);

    const handlePaymentCompletion = async (token) => {
        try {
            const response = await completePayment(token);
            if (response.status === 'success') {
                console.log('Payment completed successfully');
                navigate(`/success/${response.orderId}`);
            } else {
                console.error('Payment failed');
                navigate(`/failure/${response.orderId}`);
            }
        } catch (error) {
            console.error('Payment completion failed:', error);
            navigate('/failure');
        }
    };

    return (
        <div className='main-container '>
            <h1 className='text-sm-center'>Zahlungsabwicklung...</h1>
        </div>
    );
};

export default CapturePayment;
