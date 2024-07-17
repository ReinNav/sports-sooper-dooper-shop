import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Typography } from 'antd';
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

    const handleClickFirstHero = () => {
        console.log('first hero button clicked');
    };

    const handleClickSecondHero = () => {
        console.log('second hero button clicked');
    };

    return (
        <div className="home-container">
            <section className="hero-section">
                <div className="hero-banner1">
                    <div className="hero-text">
                        <h1>Oberteile</h1>
                        <p>Die besten Oberteile für deine Sport</p>
                        <button className="cta-button" onClick={handleClickFirstHero}>Jetzt Shoppen</button>
                    </div>
                </div>
                <div className="hero-banner2">
                    <div className="hero-text">
                        <h1>Schuhe</h1>
                        <p>Zuverlässige und hochwertige Sportschuhe</p>
                        <button className="cta-button" onClick={handleClickSecondHero}>Jetzt Shoppen</button>
                    </div>
                </div>
            </section>
            <section className="products-section">
                <div style={{padding: '20px'}}>
                    <Title id='list-title' level={1}>Unsere Produkte</Title>
                    {products.length > 0 ? (
                        <div className="product-list">
                            {products.map((product, index) => (
                                <Link to={`/product/${product.id}`} className="product-item" key={index}>
                                    <img src={product.imageLink} alt={product.name} className="product-image"/>
                                    <div className='flex-column product-detail-wrapper'>
                                    <h3>{product.name}</h3>
                                    <p id='price'>{product.price.toFixed(2).replace('.', ',')} €</p>
                                    </div>
                                </Link>
                            ))}
                        </div>
                    ) : (
                        <div>Keine Produkte noch!</div>
                    )}
                </div>
            </section>
        </div>
    );
}

export default Home;
