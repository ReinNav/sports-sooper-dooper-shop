import React, { useState } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { login } from '../actions/Auth'
import 'bootstrap/dist/css/bootstrap.min.css'; // Ensure Bootstrap CSS is imported

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const onSubmit = (event) => {
        event.preventDefault();
        setLoading(true);

        dispatch(login(username, password))
            .then(() => {
              navigate('/');
            })
            .catch((err) => {
              setLoading(false);
              setError(err);
            });
    };

    return (
        <div className='container mt-5 w-50'>
            <h2 className='text-center'>Login</h2>
            <form onSubmit={onSubmit} className="mb-3">
                <div className="mb-3">
                    <label htmlFor="username" className="form-label">Username</label>
                    <input
                        type="text"
                        className="form-control"
                        id="username"
                        value={username}
                        onChange={(event) => setUsername(event.target.value)}
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="password" className="form-label">Password</label>
                    <input
                        type="password"
                        className="form-control"
                        id="password"
                        value={password}
                        onChange={(event) => setPassword(event.target.value)}
                    />
                </div>
                <button 
                    type="sbmit"
                    className="btn btn-primary w-100"
                    disabled={loading}
                >
                    Login
                </button>
                {error && <div className="alert alert-danger mt-2">{error}</div>}
            </form>
            <p className="text-center">
                Don't have an account? <a href="/signup" className="link-primary" onClick={(e) => {
                    e.preventDefault();
                    navigate('/signup')
                }}>Sign up</a>
            </p>
        </div>
    );
};

export default Login;
