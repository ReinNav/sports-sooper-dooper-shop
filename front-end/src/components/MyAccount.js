import React, { useEffect, useState } from 'react';
import '../App.css';
import './MyAccount.css';
import { useAuth } from 'react-oidc-context';

const MyAccount = () => {
    const auth = useAuth();
    const [user, setUser] = useState(null);
    const [orders, setOrders] = useState([]);
    const [addresses, setAddresses] = useState([]);

    useEffect(() => {
        const fetchOrders = async (userId) => {
            try {
                const response = await fetch(`/api/orders/${userId}`);
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const data = await response.json();
                setOrders(data);
            } catch (error) {
                console.error('There was an error fetching the orders!', error);
            }
        };

        const fetchAddresses = async (userId) => {
            try {
                const response = await fetch(`/api/addresses/${userId}`);
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const data = await response.json();
                setAddresses(data);
            } catch (error) {
                console.error('There was an error fetching the addresses!', error);
            }
        };

        setUser(auth.user);
        if (auth.isAuthenticated && auth.user) {
            fetchOrders(auth.user.profile.sub);
            fetchAddresses(auth.user.profile.sub);
        }
    }, [auth.isAuthenticated, auth.user]);

    return (
        <div className="my-account">
            <h1>My Account</h1>
            <div className="account-details">
                <p>Welcome, {user?.profile.email || 'Guest'}.</p> 
                <div className="saved-addresses">
                    <h2>Saved Addresses</h2>
                    <ul>
                        {addresses.map((address, index) => (
                            <li key={index}>{address}</li>
                        ))}
                    </ul>
                </div>
                <div className="orders">
                    <h2>Orders</h2>
                    <ul>
                        {orders.map((order, index) => (
                            <li key={index}>Order #{order.id} - {order.status}</li>
                        ))}
                    </ul>
                </div>
            </div>
        </div>
    );
};

export default MyAccount;