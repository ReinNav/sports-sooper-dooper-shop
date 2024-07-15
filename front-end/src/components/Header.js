import React, { useEffect, useState } from 'react';
import { useAuth } from "react-oidc-context";
import { ShoppingCartOutlined } from '@ant-design/icons';
import '../App.css';
import '../Header.css';

function Header() {
    const auth = useAuth();
    const [user, setUser] = useState(null);

    useEffect(() => {
        setUser(auth.user);
    }, [auth.user]);

    const handleLogout = () => {
        auth.signoutRedirect({
            post_logout_redirect_uri: window.location.origin // Redirect to the root path after logout
        });
    };

    const handleLogin = () => {
        auth.signinRedirect({
            redirect_uri: window.location.href // Redirect back to the current page after login
        });
    };

    const handleClickSearch = () => {
        console.log('Search button clicked');
    };

    const handleCartClick = () => {
        window.location.href = '/cart';
    };

    return (
        <header className="header">
            <div className="header-content">
                <div className="logo">
                    <img src="/logo.png" alt="Logo" />
                </div>
                <nav className="navbar-category">
                    <a href="#" className="nav-link">OBERTEILE</a>
                    <a href="#" className="nav-link">HOSEN</a>
                    <a href="#" className="nav-link">SCHUHE</a>
                </nav>
                <div className="search-bar">
                    <input
                        type="text"
                        placeholder="Suche..."
                    />
                    <button className="search-button" onClick={handleClickSearch}>Suchen</button>
                </div>
                <ShoppingCartOutlined className='cart-icon' onClick={() => (handleCartClick())} />
                <div className="auth-buttons">
                    {user ? (
                        <button className="auth-button logout-button" onClick={handleLogout}>
                            Logout
                        </button>
                    ) : (
                        <button className="auth-button login-button" onClick={handleLogin}>
                            Login
                        </button>
                    )}
                </div>
            </div>
        </header>
    );
}

export default Header;
