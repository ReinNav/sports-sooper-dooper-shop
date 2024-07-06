import React, { useEffect, useState } from 'react';
import { Typography, List } from 'antd';
import ProductsApi from './ProductsApi';
import '../Home.css';

const { Title } = Typography;

function Home() {
    const [products, setProducts] = useState([]);

    const exProducts = [
        {
            name: "Deutschland Trikot",
            description: "Offizielles Heimtrikot.",
            price: 74.95,
            foto: "../logo.png"
        },
        {
            name: "Deutschland DNA Jogginghose",
            description: "Bequeme Jogginghose.",
            price: 64.95,
            foto: "../logo.png"
        },
        {
            name: "Trainingshose EM 2024",
            description: "Trainingshose für Kinder.",
            price: 59.95,
            foto: "../logo.png"
        },
        {
            name: "Rucksack Schwarz",
            description: "Praktischer Rucksack.",
            price: 49.95,
            foto: "../logo.png"
        },
        {
            name: "Hose Blau EURO",
            description: "Praktische Hose.",
            price: 29.95,
            foto: "../logo.png"
        },
        {
            name: "Rucksack Rot",
            description: "Praktischer Rucksack.",
            price: 29.95,
            foto: "../logo.png"
        }
        ,
        {
            name: "Deutschland Trikot Weiß",
            description: "Offizielles Heimtrikot.",
            price: 49.95,
            foto: "../logo.png"
        },
        {
            name: "Deutschland Trikot",
            description: "Offizielles Heimtrikot.",
            price: 74.95,
            foto: "../logo.png"
        },
        {
            name: "Deutschland DNA Jogginghose",
            description: "Bequeme Jogginghose.",
            price: 64.95,
            foto: "../logo.png"
        },
        {
            name: "Trainingshose EM 2024",
            description: "Trainingshose für Kinder.",
            price: 59.95,
            foto: "../logo.png"
        },
        {
            name: "Rucksack Schwarz",
            description: "Praktischer Rucksack.",
            price: 49.95,
            foto: "../logo.png"
        },
        {
            name: "Hose Blau EURO",
            description: "Praktische Hose.",
            price: 29.95,
            foto: "../logo.png"
        },
        {
            name: "Rucksack Rot",
            description: "Praktischer Rucksack.",
            price: 29.95,
            foto: "../logo.png"
        }
        ,
        {
            name: "Deutschland Trikot Weiß",
            description: "Offizielles Heimtrikot.",
            price: 49.95,
            foto: "../logo.png"
        },
        {
            name: "Polen Trikot Weiß",
            description: "Offizielles Heimtrikot.",
            price: 69.95,
            foto: "../logo.png"
        }
    ];

    useEffect(() => {
        setProducts(exProducts);

        /*
        const handleProducts = async () => {
            try {
                const response = await ProductsApi.getProducts();
                setProducts(response.data);
            } catch (error) {
                console.log(error);
            }
        };
        handleProducts(); */
    }, []);

    const handleClickSearch = () => {
        console.log('Search button clicked');
    };

    const handleClickFootball = () => {
        console.log('Football button clicked');
    };

    const handleClickBasketball = () => {
        console.log('Basketball button clicked');
    };

    return (
        <div className="home-container">
            <div className="search-bar">
                <input
                    type="text"
                    placeholder="Suche..."
                />
                <button className="search-button" onClick={handleClickSearch}>Suchen</button>
            </div>
            <header>
                <nav className="navbar">
                    <a href="#">FUSSBALLSCHUHE</a>
                    <a href="#">SPORTBEKLEIDUNG</a>
                    <a href="#">FAN-SHOP</a>
                    <a href="#">EQUIPMENT</a>
                    <a href="#">LIFESTYLE</a>
                    <a href="#">MEHR</a>
                </nav>
            </header>
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
