import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Typography } from 'antd';
import { useAuth } from 'react-oidc-context';
import { getOrderById } from './Api/OrderApi';
import '../styles/OrderDetail.css';

const { Title, Paragraph } = Typography;

const OrderDetail = () => {
    const { orderId } = useParams();
    const navigate = useNavigate();
    const auth = useAuth();
    const [order, setOrder] = useState(null);

    useEffect(() => {
        const fetchOrder = async () => {
            try {
                const data = await getOrderById(orderId, auth.user.access_token);
                setOrder(data);
            } catch (error) {
                console.error('There was an error fetching the order!', error);
            }
        };

        if (auth.isAuthenticated && auth.user) {
            fetchOrder();
        }
    }, [auth.isAuthenticated, auth.user, orderId]);

    const handleProductClick = (productId) => {
        navigate(`/product/${productId}`);
    };

    if (!order) {
        return <div>Loading...</div>;
    }

    const { shippingAddress, billingAddress, contactDetails, orderItems, status, shipmentType, totalAmount } = order;

    return (
        <div className="main-container order-detail">
            <Title level={1}>Order Detail</Title>
            <div className="order-summary">
                <Title level={3}>Order ID: {order.orderId}</Title>
                <Paragraph>Status: {status}</Paragraph>
                
                <Title level={3}>Shipping Address</Title>
                <Paragraph>{`${shippingAddress.firstName} ${shippingAddress.lastName}`}</Paragraph>
                <Paragraph>{`${shippingAddress.street} ${shippingAddress.houseNumber}`}</Paragraph>
                <Paragraph>{`${shippingAddress.postalCode} ${shippingAddress.city}`}</Paragraph>
                <Paragraph>{shippingAddress.country}</Paragraph>

                <Title level={3}>Billing Address</Title>
                <Paragraph>{`${billingAddress.firstName} ${billingAddress.lastName}`}</Paragraph>
                <Paragraph>{`${billingAddress.street} ${billingAddress.houseNumber}`}</Paragraph>
                <Paragraph>{`${billingAddress.postalCode} ${billingAddress.city}`}</Paragraph>
                <Paragraph>{billingAddress.country}</Paragraph>

                <Title level={3}>Products</Title>
                <ul>
                    {orderItems.map((item) => (
                        <li key={item.productId} onClick={() => handleProductClick(item.productId)}>
                            <Typography.Link>
                                Product ID: {item.productId} - {item.name} - Quantity: {item.quantity}
                            </Typography.Link>
                        </li>
                    ))}
                </ul>

                <hr />
                <div className='flex-row product-and-price total-price'>
                    <Paragraph>Shipment</Paragraph>
                    <Paragraph>{shipmentType === "EXPRESS" ? "Express: 7.99 €" : "Standard: 4.99 €"}</Paragraph>
                </div>
                <div className='flex-row product-and-price total-price'>
                    <Paragraph>Total Amount</Paragraph>
                    <Paragraph>{totalAmount.toFixed(2).replace('.', ',')} €</Paragraph>
                </div>
            </div>
        </div>
    );
};

export default OrderDetail;
