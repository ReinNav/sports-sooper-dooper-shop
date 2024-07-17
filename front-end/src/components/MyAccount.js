import React, { useEffect, useState } from 'react';
import './MyAccount.css';
import Keycloak from 'keycloak-js';

const MyAccount = () => {
    const [user, setUser] = useState(null);
    const [orders, setOrders] = useState([]);
    const [addresses, setAddresses] = useState([]);

    useEffect(() => {
        const keycloak = Keycloak('/keycloak.json');
        keycloak.init({ onLoad: 'login-required' }).then(authenticated => {
            if (authenticated) {
                keycloak.loadUserInfo().then(userInfo => {
                    setUser(userInfo);
                    fetchData(userInfo);
                });
            }
        });

        const fetchData = async (userInfo) => {
            try {
                // Fetch orders
                const ordersResponse = await fetch(`/api/orders?userId=${userInfo.sub}`);
                const ordersData = await ordersResponse.json();
                setOrders(ordersData);

                // Fetch addresses
                const addressesResponse = await fetch(`/api/addresses?userId=${userInfo.sub}`);
                const addressesData = await addressesResponse.json();
                setAddresses(addressesData);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };
    }, []);

    if (!user) {
        return <div>Loading...</div>;
    }

    return (
        <div className="my-account">
            <h1>My Account</h1>
            <div className="account-details">
                <p>Welcome, {user.preferred_username}.</p>
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

