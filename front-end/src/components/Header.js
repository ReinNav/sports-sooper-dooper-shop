import React, { useEffect, useState } from 'react';
import { useAuth } from "react-oidc-context";
import { ShoppingCartOutlined } from '@ant-design/icons';
import { useCart } from './CartContext';
import '../App.css';
import '../styles/Header.css';

function Header() {
    const auth = useAuth();
    const [user, setUser] = useState(null);
    const { cartCount, fetchCart } = useCart(); 

    useEffect(() => {
        setUser(auth.user);
        if (auth.isAuthenticated) {
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

    const handleClickSearch = () => {
        console.log('Search button clicked');
    };

    const handleCartClick = () => {
        window.location.href = '/cart';
    };

    
    const handleLogoClick = () => {
        window.location.href = '/';
    };

    return (
        <header className="header">
            <div className="header-content">
                <div className="logo">
                    <img src="/logo.png" alt="Logo" onClick={handleLogoClick} />
                </div>
                <nav className="navbar-category">
                    <a href="#" className="nav-link">OBERTEILE</a>
                    <a href="#" className="nav-link">HOSEN</a>
                    <a href="#" className="nav-link">SCHUHE</a>
                    <a href="/products" className="nav-link">MEHR</a>
                </nav>
                <div className='flex-row header-second-half'>
                    <div className="search-bar">
                        <input
                            type="text"
                            placeholder="Suche..."
                        />
                        <button className="search-button" onClick={handleClickSearch}>Suchen</button>
                    </div>
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
