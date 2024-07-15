import React, { useEffect, useState, useCallback } from 'react';
import { useAuth } from 'react-oidc-context';
import { getCart, addToCart, subtractFromCart, removeFromCart, clearCart } from './Api/CartApi';
import '../Cart.css';

const Cart = () => {
    const auth = useAuth();
    const [cart, setCart] = useState(null);

    const fetchCart = useCallback(async () => {
        try {
            const userId = auth.user.profile.sub;
            const cartData = await getCart(userId);
            console.log(cartData);
            setCart(cartData);
        } catch (error) {
            console.error('Failed to fetch cart', error);
        }
    }, [auth]);

    useEffect(() => {
        if (auth.isAuthenticated) {
            fetchCart();
        }
    }, [auth.isAuthenticated, fetchCart]);

    const handleAdd = async (cartItem) => {
        try {
            const userId = auth.user.profile.sub;
            const updatedCart = await addToCart(userId, cartItem, 1);
            setCart(updatedCart);
        } catch (error) {
            console.error('Failed to add to cart', error);
        }
    };

    const handleSubtract = async (cartItem) => {
        try {
            const userId = auth.user.profile.sub;
            const updatedCart = await subtractFromCart(userId, cartItem, 1);
            setCart(updatedCart);
        } catch (error) {
            console.error('Failed to subtract from cart', error);
        }
    };

    const handleRemove = async (productId) => {
        try {
            const userId = auth.user.profile.sub;
            const updatedCart = await removeFromCart(userId, productId);
            setCart(updatedCart);
        } catch (error) {
            console.error('Failed to remove from cart', error);
        }
    };

    const handleClear = async () => {
        try {
            const userId = auth.user.profile.sub;
            const updatedCart = await clearCart(userId);
            setCart(updatedCart);
        } catch (error) {
            console.error('Failed to clear cart', error);
        }
    };

    if (!cart) {
        return <div>Loading...</div>;
    }

    // Convert cartItems map to an array of { item, quantity } pairs
    const cartItemsArray = Object.entries(cart.cartItems).map(([key, quantity]) => {
        const item = parseCartItemKey(key);
        return { item, quantity };
    });

    return (
        <div className="cart">
            <h1>Shopping Cart</h1>
            <div className='flex-row cart-detail'>
            {cartItemsArray.length === 0 ? (
                <p>Your cart is empty</p>
            ) : (
                cartItemsArray.map(({ item, quantity }) => (
                    <div key={item.id} className="flex-row cart-item">
                        <div className='flex-row product-row-1'>
                            <img src={item.imageLink === "null" ? '/no-image.png' : item.imageLink} alt="Logo" className='product-image'/>
                            <div className='flex-column product-info'>
                                <h2 className='product-name'>{item.name}</h2>
                                <span className='price'>{item.price.toFixed(2) +" €"}</span>
                                <div className='flex-row product-detail-first-row'>
                                    <p className='product-detail'>Size: {item.size}</p>
                                    <p className='product-detail'>Color: {item.colour}</p>
                                </div>
                                <p className='product-detail'>{item.gender}'s</p>
                            </div>
                        </div>
                        <div className='flex-column product-row-2'>
                            <div className="cart-item-actions">
                                    <div className='flex-row plus-minus-container'>
                                        <button onClick={() => handleSubtract(item)}>-</button>
                                        <div className="quantity-display">{quantity}</div>
                                        <button onClick={() => handleAdd(item)}>+</button>
                                    </div>
                                    <button onClick={handleRemove(item.id)} className='remove-item'>
                                        <img src='/trash.svg' alt="remove item" />
                                    </button>
                            </div>
                        </div>
                    </div>
                ))
            )}
            </div>
            {cartItemsArray.length !== 0 && (
                <button onClick={handleClear} className="clear-cart-button">Clear Cart</button>
            )}
        </div>
    );
};

const parseCartItemKey = (key) => {
    const regex = /CartItem\((.*)\)/;
    const match = key.match(regex);

    if (!match) {
        throw new Error('Invalid cart item key format');
    }

    const properties = match[1].split(', ').reduce((acc, prop) => {
        const [key, value] = prop.split('=');
        acc[key] = value;
        return acc;
    }, {});

    return {
        id: properties.id,
        productId: properties.productId,
        name: properties.name,
        price: parseFloat(properties.price),
        gender: properties.gender,
        size: properties.size,
        colour: properties.colour,
        imageLink: properties.imageLink || null,
    };
};

export default Cart;
