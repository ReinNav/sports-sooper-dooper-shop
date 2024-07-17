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
            const paymentStatus = await completePayment(token);
            if (paymentStatus === 'success') {
                console.log('Payment completed successfully');
                navigate('/success/{$orderid}');
            } else {
                console.error('Payment failed');
                navigate('/failure');
            }
        } catch (error) {
            console.error('Payment completion failed:', error);
            navigate('/failure');
        }
    };

    return (
        <div>
            <p>Processing payment...</p>
        </div>
    );
};

export default CapturePayment;
