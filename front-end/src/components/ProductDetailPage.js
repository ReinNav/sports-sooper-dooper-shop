import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import './ProductDetailPage.css';
import { Modal, Button } from 'antd';
import ProductsApi from './Api/ProductsApi';
import { addToCart } from './Api/CartApi'; 
import { useAuth } from 'react-oidc-context'; 
import { useCart } from './CartContext'; 

const ProductDetailPage = () => {
    const { id } = useParams(); 
    const auth = useAuth(); 
    const { fetchCart } = useCart(); 
    const navigate = useNavigate();
    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(true);
    const [quantity, setQuantity] = useState(1);
    const [isModalVisible, setIsModalVisible] = useState(false);

    const fetchProduct = async () => {
        try {
            const response = await ProductsApi.getProduct(id);
            setProduct(response.data);
            setLoading(false);
        } catch (error) {
            console.error('There was an error fetching the product!', error);
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchProduct();
    }, [id]);

    const handleQuantityChange = (amount) => {
        setQuantity((prevQuantity) => {
            const newQuantity = prevQuantity + amount;
            return newQuantity > 0 ? newQuantity : 1;
        });
    };

    const handleAddToCart = async () => {
        if (!auth.isAuthenticated) {
            console.error('User is not authenticated');
            return;
        }

        const cartItem = {
            productId: product.id,
            name: product.name,
            price: product.price,
            gender: product.gender,
            size: product.size,
            colour: product.colour,
            imageLink: product.imageLink,
            quantity,
        };

        try {
            await addToCart(auth.user?.profile.sub, cartItem, quantity); 
            fetchCart(auth.user.profile.sub);
            setQuantity(1);
            await fetchProduct(); // Refresh product data
            setIsModalVisible(true); // Show modal after adding to cart
        } catch (error) {
            console.error('Failed to add product to cart', error);
        }
    };

    const handleCartNavigation = () => {
        navigate('/cart');
    };

    const handleContinueShopping = () => {
        setIsModalVisible(false);
    };

    if (loading) {
        return <div>Loading...</div>;
    }

    if (!product) {
        return <div>Product not found</div>;
    }

    return (
        <div className="container">
            <div className='flex-column inner-container'>
                <div className='flex-row first-row'>
                    <img className='product-detail-image' src={product.imageLink} alt={product.name} />
                    <div className="flex-column product-details">
                        <h1>{product.name}</h1>
                        <div className="rating">
                            {'★'.repeat(5)}
                        </div>
                        <div className="price">{product.price.toFixed(2).replace('.', ',')} €</div>
                        <div className="flex-column color-size-detail">
                            <div className="size">Größe: {product.size}</div>
                            <div className="colour">Farbe: {product.colour}</div>
                        </div>
                        <div className='flex-column product-detail-buttons'>
                            {product.amount === 0 && <p style={{ color: 'red' }}>Leider nicht auf Lager</p>}
                            <div className='flex-row product-detail-plus-minus-container'>
                                <button onClick={() => handleQuantityChange(-1)} disabled={quantity === 1}>-</button>
                                <div className="quantity-display">{quantity}</div>
                                <button onClick={() => handleQuantityChange(1)} disabled={quantity >= product.amount}>+</button>
                            </div>
                            <button className="add-to-cart" onClick={handleAddToCart} disabled={product.amount === 0}>Zum Warenkorb hinzufügen</button>
                            <button className="wishlist">&#9829;</button>
                        </div>
                    </div>
                </div>
                <div className="product-description">
                    <h2>Beschreibung</h2>
                    <p>{product.description}</p>
                </div>
            </div>
            <Modal
                title="Produkt zum Warenkorb hinzugefügt"
                open={isModalVisible}
                onCancel={handleContinueShopping}
                footer={[
                    <Button className='modal-primary-button' key="cart" type="primary" onClick={handleCartNavigation}>
                        Zum Warenkorb
                    </Button>,
                    <Button key="continue" onClick={handleContinueShopping}>
                        Weiter Shoppen
                    </Button>
                ]}
            >
                <p>Das Produkt wurde erfolgreich zum Warenkorb hinzugefügt.</p>
            </Modal>
        </div>
    );
};

export default ProductDetailPage;
