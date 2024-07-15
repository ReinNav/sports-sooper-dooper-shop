import React, { createContext, useState, useEffect, useContext, useCallback } from 'react';
import { getCart } from './Api/CartApi';

const CartContext = createContext();

export const CartProvider = ({ children }) => {
    const [cart, setCart] = useState(null);
    const [cartCount, setCartCount] = useState(0);

    const fetchCart = useCallback(async (userId) => {
        try {
            const cartData = await getCart(userId);
            const totalAmount = cartData.cartItems.reduce((total, item) => total + item.quantity, 0);
            setCart(cartData);
            setCartCount(totalAmount);
        } catch (error) {
            console.error('Failed to fetch cart', error);
        }
    }, []);

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
