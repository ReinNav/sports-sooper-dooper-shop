import React, { createContext, useState, useContext, useCallback } from 'react';
import { useAuth } from 'react-oidc-context';
import { getCart } from './Api/CartApi';

const CartContext = createContext();

export const CartProvider = ({ children }) => {
    const [cart, setCart] = useState(null);
    const [cartCount, setCartCount] = useState(0);
    const auth = useAuth(); 

    const fetchCart = useCallback(async (userId) => {
        if (!auth.user) return; 

        try {
            const token = auth.user.access_token; 
            const cartData = await getCart(userId, token); 
            const totalAmount = cartData.cartItems.reduce((total, item) => total + item.quantity, 0);
            setCart(cartData);
            setCartCount(totalAmount);
        } catch (error) {
            console.error('Failed to fetch cart', error);
        }
    }, [auth.user]);

    const updateCart = async (userId) => {
        await fetchCart(userId);
    };

    return (
        <CartContext.Provider value={{ cart, cartCount, fetchCart, updateCart, setCart }}>
            {children}
        </CartContext.Provider>
    );
};

export const useCart = () => useContext(CartContext);
