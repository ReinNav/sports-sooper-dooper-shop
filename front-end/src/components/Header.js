import React, { useEffect, useState } from 'react';
import { useAuth } from "react-oidc-context";
import { Button } from 'antd';

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

    return (
        <div className="container-fluid bg-light">
            <div className="row align-items-center">
                <div className="col">
                    <img src="/logo192.png" alt="Logo" className="img-fluid" style={{ maxHeight: '50px' }} />
                </div>
                <div className="col text-center">
                    {user ? (
                        <span>Hi {user?.profile.preferred_username}</span>
                    ) : (
                        <span>Welcome!</span>
                    )}
                </div>
                <div className="col text-end">
                    {user ? (
                        <Button
                            type="primary"
                            size="small"
                            onClick={handleLogout}
                            danger
                        >
                            Logout
                        </Button>
                    ) : (
                        <Button
                            type="primary"
                            size="small"
                            onClick={handleLogin}
                            danger
                        >
                            Login
                        </Button>
                    )}
                </div>
            </div>
        </div>
    );
}

export default Header;
