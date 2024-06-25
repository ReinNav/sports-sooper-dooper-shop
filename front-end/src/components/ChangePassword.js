import React, { useState } from 'react';
import { useDispatch, useSelector
 } from 'react-redux';
import { changePassword } from '../actions/Auth';
import { useNavigate } from 'react-router-dom';

const SignUp = () => {
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const user = useSelector((state) => state.auth.user);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const onSubmit = (event) => {
        event.preventDefault();
        setLoading(true);

        dispatch(changePassword(user.username, oldPassword, newPassword))
            .then(() => {
                console.log("success")
              navigate('/');
            })
            .catch((err) => {
              setLoading(false);
              setError(err);
            });
    };

    return (
        <div className='container mt-5 w-50'>
            <h2 className='text-center'>Change password</h2>
            <form onSubmit={onSubmit} className="mb-3">
                <div className="mb-3">
                    <label htmlFor="oldPassword" className="form-label">Old Password</label>
                    <input
                        type="password"
                        className="form-control"
                        id="oldPassword"
                        value={oldPassword}
                        onChange={(event) => setOldPassword(event.target.value)}
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="password" className="form-label">Password</label>
                    <input
                        type="password"
                        className="form-control"
                        id="password"
                        value={newPassword}
                        onChange={(event) => setNewPassword(event.target.value)}
                    />
                </div>
                <button 
                    type="sbmit"
                    className="btn btn-primary w-100"
                    disabled={loading}
                >
                    Change password
                </button>
                {error && <div className="alert alert-danger mt-2">{error}</div>}
            </form>
        </div>
    );
};

export default SignUp;
