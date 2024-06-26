import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { logout } from '../actions/Auth';

const Landing = () => {
    const dispatch = useDispatch();
    const isAuthenticated = useSelector((state) => state.auth.isLoggedIn);
    const user = useSelector((state) => state.auth.user);
    const navigate = useNavigate();

    const handleLogout = () => {
        dispatch(logout());
        navigate("/")
    }

    const handleLogin = () => {
        navigate("/login")
    }

    const handleMyAccount = () => {
        navigate("/myaccount")
    }

    return (
        <div style={{ fontSize: '24px' }}>
            {isAuthenticated ? (
                <>
                <h1>You are logged in! Welcome {user.username}</h1>
                <button onClick={() => handleLogout()}>Logout</button>
                <button onClick={() => handleMyAccount()}>My Account</button>
                </>
            ) : (
                <>
                <h1>Please log in.</h1>
                <button onClick={() => handleLogin()}>Login</button>
                </>
            )}
        </div>
    );
};

export default Landing;