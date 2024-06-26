import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './components/Login';
import SignUp from './components/SignUp';
import ChangePassword from './components/ChangePassword';
import Landing from './components/Landing';
import ProtectedRoute from './components/ProtectedRoute'
import MyAccount from './components/MyAccount';

const AppRoutes = () => (
    <Router>
        <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<SignUp />} />
            <Route path="/change-password" element={<ProtectedRoute><ChangePassword /></ProtectedRoute>} />
            <Route path="/myaccount" element={<ProtectedRoute><MyAccount /></ProtectedRoute>} />
            <Route path="/" element={<Landing />} />
        </Routes>
    </Router>
);

export default AppRoutes;
