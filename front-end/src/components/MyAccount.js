import React, { useEffect, useState } from 'react';
import './MyAccount.css';
import { useAuth } from 'react-oidc-context';


const MyAccount = () => {
    const auth = useAuth();
    const [user, setUser] = useState(null);
    const [orders, setOrders] = useState([]);
    const [addresses, setAddresses] = useState([]);

    useEffect(() => {
        setUser(auth.user);
        if (auth.isAuthenticated) {
    
        }
    })
        

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

