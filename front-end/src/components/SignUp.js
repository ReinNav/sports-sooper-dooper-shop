import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { signup, login } from '../actions/Auth';
import { useNavigate } from 'react-router-dom';

const SignUp = () => {
    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const onSubmit = (event) => {
        event.preventDefault();
        setLoading(true);

        dispatch(signup(username, email, password))
            .then(() => {
                dispatch(login(username, password))
              navigate('/');
            })
            .catch((message) => {
              setLoading(false);
              setError(message);
            });
    };

    return (
        <div className='container mt-5 w-50'>
            <h2 className='text-center'>Sign up</h2>
            <form onSubmit={onSubmit} className="mb-3">
                <div className="mb-3">
                    <label htmlFor="email" className="form-label">Email</label>
                    <input
                        type="text"
                        className="form-control"
                        id="email"
                        value={email}
                        onChange={(event) => setEmail(event.target.value)}
                    />
                </div>
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
                    Sign up
                </button>
                {error && <div className="alert alert-danger mt-2">{error}</div>}
            </form>
            <p className="text-center">
                Already have an account? <a href="/login" className="link-primary" onClick={(e) => {
                    e.preventDefault();
                    navigate('/login');
                }}>Login</a>
            </p>
        </div>
    );
};

export default SignUp;
