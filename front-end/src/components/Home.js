import React, { useEffect, useState } from 'react';
import { Typography, List } from 'antd';
import ProductsApi from './Api/ProductsApi';
import '../Home.css';

const { Title } = Typography;

function Home() {
    const [products, setProducts] = useState([]);

    useEffect(() => {
        const handleProducts = async () => {
            try {
                const response = await ProductsApi.getProducts();
                setProducts(response.data);
            } catch (error) {
                console.log(error);
            }
        };
        handleProducts();
    }, []);

    const handleClickFootball = () => {
        console.log('Football button clicked');
    };

    const handleClickBasketball = () => {
        console.log('Basketball button clicked');
    };

    return (
        <div className="home-container">
            <section className="hero-section">
                <div className="hero-banner1">
                    <div className="hero-text">
                        <h1>Football Equipment</h1>
                        <p>Football Equipment & More</p>
                        <button className="cta-button" onClick={handleClickFootball}>Jetzt Shoppen</button>
                    </div>
                </div>
                <div className="hero-banner2">
                    <div className="hero-text">
                        <h1>Basketball Equipment</h1>
                        <p>Basketball Equipment & More</p>
                        <button className="cta-button" onClick={handleClickBasketball}>Jetzt Shoppen</button>
                    </div>
                </div>
            </section>
            <section className="products-section">
                <div style={{padding: '20px'}}>
                    <Title level={2}>Product Highlights</Title>
                    {products.length > 0 ? (
                        <div className="product-list">
                            {products.map((product, index) => (
                                <div className="product-item" key={index}>
                                    <img src={product.foto} alt={product.name} className="product-image"/>
                                    <h3>{product.name}</h3>
                                    <p>{product.description}</p>
                                    <p>{product.price} €</p>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div>No products yet!</div>
                    )}
                </div>
            </section>
            <footer className="footer-section">
                <p>&copy; 2024 Sport Super Dooper Store. Alle Rechte vorbehalten.</p>
            </footer>
        </div>
    );
}

export default Home;
