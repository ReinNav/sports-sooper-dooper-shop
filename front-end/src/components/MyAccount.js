import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { logout } from '../actions/Auth';

const MyAccount = () => {
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

    return (
        <div style={{ fontSize: '24px' }}>
            {isAuthenticated ? (
                <>
                <h1>My Account: Welcome {user.username}</h1>
                <button onClick={() => handleLogout()}>Logout</button>
                <p className="text-center">
                Forgot password? <a href="/change-password" className="link-primary" onClick={(e) => {
                    e.preventDefault();
                    navigate('/change-password')
                }}>Change password</a>
            </p>
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

export default MyAccount;
