import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Typography } from 'antd';
import { useAuth } from 'react-oidc-context';
import { getOrdersByUserId } from './Api/OrderApi';
import '../styles/MyAccount.css';

const { Title, Paragraph } = Typography;

const MyAccount = () => {
    const auth = useAuth();
    const [user, setUser] = useState(null);
    const [orders, setOrders] = useState([]);

    useEffect(() => {
        const fetchOrders = async (userId, token) => {
            try {
                const data = await getOrdersByUserId(userId, token);
                setOrders(data);
            } catch (error) {
                console.error('There was an error fetching the orders!', error);
            }
        };

        if (auth.isAuthenticated && auth.user) {
            setUser(auth.user);
            fetchOrders(auth.user.profile.sub, auth.user.access_token);
        }
    }, [auth.isAuthenticated, auth.user]);

    const getStatusStyle = (status) => {
        switch (status) {
            case 'PENDING':
                return { color: 'blue' };
            case 'CONFIRMED':
                return { color: 'green' };
            default:
                return {};
        }
    };

    return (
        <div className="main-container my-account">
            <Title level={1} className='text-center'>My Account</Title>
            <div className="flex-column account-details">
                <Paragraph>Welcome, {user?.profile.given_name || 'Guest'}.</Paragraph>
                <div className="profile-form">
                    <div className='flex-column'>
                        <label>Vorname:</label>
                        <input 
                            type="text" 
                            name="given_name" 
                            value={user?.profile.given_name} 
                            disabled
                        />
                    </div>
                    <div className='flex-column'>
                        <label>Nachname:</label>
                        <input 
                            type="text" 
                            name="family_name" 
                            value={user?.profile.family_name} 
                            disabled
                        />
                    </div>
                    <div className='flex-column'>
                        <label>Email:</label>
                        <input 
                            type="email" 
                            name="email" 
                            value={user?.profile.email} 
                            disabled 
                        />
                    </div>
                </div>
                <div className="orders">
                    <Title className="text-sm-center" level={2}>Bestellhistorie</Title>
                    <ul>
                        {orders.map((order) => (
                            <li key={order.id}>
                                <Link className="order-link" to={`/order/${order.orderId}`}>
                                    <div className='flex-row order-child'>
                                        <div>
                                            Order #{order.orderId}  
                                        </div>
                                        <div style={getStatusStyle(order.status)}>
                                            {order.status} 
                                        </div>
                                    </div>
                                </Link>
                            </li>
                        ))}
                    </ul>
                </div>
            </div>
        </div>
    );
};

export default MyAccount;
