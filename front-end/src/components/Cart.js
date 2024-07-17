import React, { useEffect, useCallback, useState } from 'react';
import { useAuth } from 'react-oidc-context';
import { useNavigate } from 'react-router-dom'; 
import { getCart, addToCart, subtractFromCart, removeFromCart, clearCart } from './Api/CartApi';
import ProductsApi from './Api/ProductsApi';
import { useCart } from './CartContext';
import '../styles/Cart.css';

const Cart = () => {
    const auth = useAuth();
    const { cart, setCart, updateCart } = useCart();
    const navigate = useNavigate(); 
    const [productQuantities, setProductQuantities] = useState({});

    const fetchCart = useCallback(async () => {
        if (auth.isAuthenticated) {
            try {
                const userId = auth.user.profile.sub;
                const cartData = await getCart(userId);
                setCart(cartData);
                fetchProductQuantities(cartData.cartItems);
            } catch (error) {
                console.error('Fehler beim Abrufen des Warenkorbs', error);
            }
        } else {
            setCart({
                cartItems: [],
                totalPrice: 0,
            });
        }
    }, [auth, setCart]);

    const fetchProductQuantities = async (cartItems) => {
        const quantities = {};
        for (const item of cartItems) {
            try {
                console.log(item.productId);
                const response = await ProductsApi.getProduct(item.productId);
                console.log(response.data.amount)
                quantities[item.productId] = response.data.amount;
            } catch (error) {
                console.error('Fehler beim Abrufen der Produktmenge', error);
            }
        }
        setProductQuantities(quantities);
    };


    useEffect(() => {
        fetchCart();
    }, [auth.isAuthenticated, fetchCart]);

    const handleAdd = async (cartItem) => {
        try {
            const userId = auth.user.profile.sub;
            await addToCart(userId, cartItem, 1);
            updateCart(userId);
            fetchProductQuantities(cart.cartItems);
        } catch (error) {
            console.error('Fehler beim Hinzufügen zum Warenkorb', error);
        }
    };

    const handleSubtract = async (cartItem) => {
        try {
            const userId = auth.user.profile.sub;
            await subtractFromCart(userId, cartItem, 1);
            updateCart(userId); 
            fetchProductQuantities(cart.cartItems);
        } catch (error) {
            console.error('Fehler beim Entfernen aus dem Warenkorb', error);
        }
    };

    const handleRemove = async (productId) => {
        try {
            const userId = auth.user.profile.sub;
            await removeFromCart(userId, productId);
            updateCart(userId);
        } catch (error) {
            console.error('Fehler beim Entfernen aus dem Warenkorb', error);
        }
    };

    const handleClear = async () => {
        try {
            const userId = auth.user.profile.sub;
            await clearCart(userId);
            updateCart(userId);
        } catch (error) {
            console.error('Fehler beim Leeren des Warenkorbs', error);
        }
    };

    const handleCheckout = () => {
        navigate('/checkout');
    };

    const handleLogin = () => {
        auth.signinRedirect({
            redirect_uri: window.location.href
        });
    };

    if (!cart) {
        return <div>Wird geladen...</div>;
    }

    return (
        <div className="main-container cart">
            <h1>Einkaufswagen</h1>
            <div className='flex-row cart-detail'>
                <div className='flex-column cart-items'>
                    {auth.isAuthenticated ? (
                        cart.cartItems.length === 0 ? (
                            <div className='flex-column empty-cart'>
                                <p className='empty-message'>Ihr Warenkorb ist leer</p>
                                <button onClick={() => window.location.href = '/'}>Zum Shop</button>
                            </div>
                        ) : (
                            cart.cartItems.map((cartItem) => (
                                <div key={cartItem.id} className="flex-row cart-item">
                                    <div className='flex-row product-row-1'>
                                        <img src={cartItem.imageLink === "null" ? '/no-image.png' : cartItem.imageLink} alt="Logo" className='product-image' />
                                        <div className='flex-column product-info'>
                                            <h2 className='product-name'>{cartItem.name}</h2>
                                            <span className='price'>{cartItem.price.toFixed(2).replace('.', ',') + " €"}</span>
                                            <div className='flex-row product-detail-first-row'>
                                                <p className='product-detail'>Größe: {cartItem.size}</p>
                                                <p className='product-detail'>Farbe: {cartItem.colour}</p>
                                            </div>
                                            <p className='product-detail'>{cartItem.gender}</p>
                                        </div>
                                    </div>
                                    <div className='flex-column product-row-2'>
                                        <div className="cart-item-actions">
                                            <div className='flex-row plus-minus-container'>
                                                <button onClick={() => handleSubtract(cartItem)}>-</button>
                                                <div className="quantity-display">{cartItem.quantity}</div>
                                                <button onClick={() => handleAdd(cartItem)} disabled={(productQuantities[cartItem.productId] === 0)}>+</button>
                                            </div>
                                            <button onClick={() => handleRemove(cartItem.productId)} className='remove-item'>
                                                <img src='/trash.svg' alt="Artikel entfernen" />
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            ))
                        )
                    ) : (
                        <div className='flex-column empty-cart'>
                            <p className='empty-message'>Bitte melden Sie sich an, um Produkte in den Warenkorb zu legen.</p>
                            <button className='auth-button login-button' onClick={handleLogin}>Login</button>
                        </div>
                    )}
                    {auth.isAuthenticated && cart.cartItems.length !== 0 && (
                        <div className='flex-row clear-cart-button-wrapper'>
                            <button onClick={handleClear} className="clear-cart-button">Warenkorb leeren</button>
                        </div>
                    )}
                </div>
                {auth.isAuthenticated && cart.cartItems.length !== 0 && (
                    <div className='flex-column cart-summary'>
                        <div className='flex-column summary'>
                            <h2>Zusammenfassung</h2>
                            <div className='flex-column summary-column'>
                                <div className='flex-row label-price'>
                                    <p>Bestellsumme</p>
                                    <p>{cart.totalPrice.toFixed(2).replace('.', ',') + " €"}</p>
                                </div>
                                <div className='flex-row label-price'>
                                    <p>Versand</p>
                                    <p>{"4,99 €"}</p>
                                </div>
                                <hr></hr>
                                <div className='flex-row label-price'>
                                    <p className='total'>Gesamt</p>
                                    <p className='total'>{(cart.totalPrice + 4.99).toFixed(2).replace('.', ',') + " €"}</p>
                                </div>
                            </div>
                            <button className='checkout-button' onClick={handleCheckout}>Zur Kasse</button>
                        </div>
                    </div>
                )}
                {!auth.isAuthenticated && (
                    <div className='flex-column cart-summary'>
                        <div className='flex-column summary'>
                            <h2>Zusammenfassung</h2>
                            <div className='flex-column summary-column'>
                                <div className='flex-row label-price'>
                                    <p>Bestellsumme</p>
                                    <p>{"0,00 €"}</p>
                                </div>
                                <div className='flex-row label-price'>
                                    <p>Versand</p>
                                    <p>{"0,00 €"}</p>
                                </div>
                                <hr></hr>
                                <div className='flex-row label-price'>
                                    <p className='total'>Gesamt</p>
                                    <p className='total'>{"0,00 €"}</p>
                                </div>
                            </div>
                            <button className='checkout-button' disabled={!auth.isAuthenticated}>Zur Kasse</button>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default Cart;
