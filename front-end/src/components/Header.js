import React, { useEffect, useState } from 'react';
import { useAuth } from "react-oidc-context";
import { ShoppingCartOutlined, UserOutlined } from '@ant-design/icons';
import { useCart } from './CartContext';
import '../App.css';
import '../styles/Header.css';

function Header() {
    const auth = useAuth();
    const [user, setUser] = useState(null);
    const { cartCount, fetchCart } = useCart();
    const [searchText, setSearchText] = useState('');

    useEffect(() => {
        setUser(auth.user);
        if (auth.isAuthenticated) {
            console.log(auth.user?.access_token);
            console.log(auth)
            fetchCart(auth.user.profile.sub);
        }
    }, [auth.user, auth.isAuthenticated, fetchCart]);

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

    const handleClickSearch = (category) => {
        window.location.href = '/products?search=' + encodeURIComponent(searchText);
    };

    const handleCartClick = () => {
        window.location.href = '/cart';
    };
    const handleLogoClick = () => {
        window.location.href = '/';
    };

    const handleAccountClick = () => {
        window.location.href = '/myaccount';
    };

    return (
        <header className="header">
            <div className="header-content">
                <div className="logo" onClick={handleLogoClick} style={{ cursor: 'pointer' }}>
                    <img src="/logo.png" alt="Logo" />
                </div>
                <nav className="navbar-category">
                    <a href="/products?category=OBERTEILE" className="nav-link">OBERTEILE</a>
                    <a href="/products?category=HOSEN" className="nav-link">HOSEN</a>
                    <a href="/products?category=SCHUHE" className="nav-link">SCHUHE</a>
                    <a href="/products" className="nav-link">MEHR</a>
                </nav>
                <div className='flex-row header-second-half'>
                    <div className="search-bar">
                        <input
                            type="text"
                            placeholder="Suche..."
                            value={searchText}
                            onChange={(e) => setSearchText(e.target.value)}
                        />
                        <button className="search-button" onClick={handleClickSearch}>Suchen</button>
                    </div>
                    <UserOutlined className='account-icon' onClick={handleAccountClick} style={{ }} />
                    <div className="cart-icon-container">
                        {cartCount > 0 && <span className='badge badge-warning' id='lblCartCount'>{cartCount}</span>}
                        <ShoppingCartOutlined className='cart-icon' onClick={handleCartClick} />
                    </div>
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
            </div>
        </header>
    );
}

export default Header;
