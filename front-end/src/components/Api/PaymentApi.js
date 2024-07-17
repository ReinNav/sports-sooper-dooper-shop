import axios from 'axios';

const PAYMENT_API_URL = 'http://localhost:1236/paypal';

export const createPayment = async (sum, userId, orderId) => {
    const response = await axios.post(`${PAYMENT_API_URL}/init`, null, {
        params: { sum, userId, orderId }
    });
    return response.data;
};

export const completePayment = async (token) => {
    const response = await axios.post(`${PAYMENT_API_URL}/capture`, null, {
        params: { token }
    });
    console.log(response);
    console.log(response.data);
    return response.data;
};
