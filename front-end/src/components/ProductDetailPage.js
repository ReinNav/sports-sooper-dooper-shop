import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import './ProductDetailPage.css';
import logo from '../logo.png'; // Logo befindet

const ProductDetailPage = () => {
    const { id } = useParams(); // Verwenden von useParams, um den Produkt-ID-Parameter zu erhalten
    const [product, setProduct] = useState({
        name: 'Beispielprodukt',
        rating: 4,
        price: 15.00,
        image: logo, // Verwenden des Logos als Produktbild
        description: 'Dies ist eine Produktbeschreibung für das Beispielprodukt.',
    });
    const [loading, setLoading] = useState(true); // Initial auf true gesetzt

    useEffect(() => {
        // Fetch-Aufruf zur API
        fetch(`/sendProduct/${id}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                setProduct(data);
                setLoading(false);
            })
            .catch(error => {
                console.error('There was an error fetching the product!', error);
                setLoading(false);
            });
    }, [id]);

    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <div className="container">
            <div className="product-image">
                <img src={product.image} alt={product.name} />
            </div>
            <div className="product-details">
                <h1>{product.name}</h1>
                <div className="rating">
                    {'★'.repeat(product.rating) + '☆'.repeat(5 - product.rating)}
                </div>
                <div className="price">${product.price.toFixed(2)}</div>
                <div className="options">
                    <div className="option-group">
                        <label>Color</label>
                        <div className="option-buttons">
                            <button>A</button>
                            <button>B</button>
                            <button>C</button>
                        </div>
                    </div>
                    <div className="option-group">
                        <label>Size</label>
                        <div className="option-buttons">
                            <button>A</button>
                            <button>B</button>
                            <button>C</button>
                        </div>
                    </div>
                </div>
                <button className="add-to-cart">Add To Cart</button>
                <button className="wishlist">&#9829;</button>
            </div>
            <div className="product-description">
                <h2>Product description</h2>
                <p>{product.description}</p>
            </div>
        </div>
    );
};

export default ProductDetailPage;
