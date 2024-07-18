import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from "react-router-dom";
import ProductsApi from './Api/ProductsApi';
import '../styles/ProductListing.css';
import { Link } from 'react-router-dom';
import { Typography } from 'antd';

const { Title } = Typography;

const useQuery = () => {
    return new URLSearchParams(useLocation().search);
};

const ProductListing = () => {
    const [products, setProducts] = useState([]);
    const [filteredProducts, setFilteredProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState('');
    const [genders, setGenders] = useState([]);
    const [selectedGender, setSelectedGender] = useState('');
    const [colors, setColors] = useState([]);
    const [selectedColor, setSelectedColor] = useState('');
    const [sizes, setSizes] = useState([]);
    const [selectedSize, setSelectedSize] = useState('');
    const [selectedPriceRange, setSelectedPriceRange] = useState('');
    const [searchTerm, setSearchTerm] = useState('');
    const [sortOption, setSortOption] = useState('');
    const query = useQuery();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const response = await ProductsApi.getProducts();
                const products = response.data;
                setProducts(products);
                setFilteredProducts(products);
                
                const uniqueCategories = [...new Set(products.map(product => product.category))];
                const uniqueGenders = [...new Set(products.map(product => product.gender))];
                const uniqueColors = [...new Set(products.map(product => product.colour))];
                const uniqueSizes = [...new Set(products.map(product => product.size))];
                setCategories(uniqueCategories);
                setGenders(uniqueGenders);
                setColors(uniqueColors);
                setSizes(uniqueSizes);

                const categoryFromUrl = query.get('category');
                if (categoryFromUrl) {
                    setSelectedCategory(categoryFromUrl);
                }

                const searchFromUrl = query.get('search');
                if (searchFromUrl) {
                    setSearchTerm(searchFromUrl);
                }
            } catch (error) {
                console.error('Error fetching products:', error);
            }
        };

        fetchProducts();
    }, []);

    useEffect(() => {
        let filtered = products;
    
        if (selectedCategory) {
            filtered = filtered.filter(product => product.category === selectedCategory);
        }
    
        if (selectedGender) {
            filtered = filtered.filter(product => product.gender === selectedGender);
        }
    
        if (selectedColor) {
            filtered = filtered.filter(product => product.colour === selectedColor);
        }
    
        if (selectedSize) {
            filtered = filtered.filter(product => product.size === selectedSize);
        }
    
        if (selectedPriceRange) {
            const [min, max] = selectedPriceRange.split('-').map(Number);
            filtered = filtered.filter(product => product.price >= min && product.price <= max);
        }
    
        if (searchTerm) {
            filtered = filtered.filter(product => product.name.toLowerCase().includes(searchTerm.toLowerCase()));
        }
    
        if (sortOption === 'name-asc') {
            filtered.sort((a, b) => a.name.localeCompare(b.name));
        } else if (sortOption === 'name-desc') {
            filtered.sort((a, b) => b.name.localeCompare(a.name));
        } else if (sortOption === 'price-asc') {
            filtered.sort((a, b) => a.price - b.price);
        } else if (sortOption === 'price-desc') {
            filtered.sort((a, b) => b.price - a.price);
        }
    
        setFilteredProducts(filtered);
    }, [selectedCategory, selectedGender, selectedColor, selectedSize, selectedPriceRange, searchTerm, sortOption, products]);
    

    const handleCategoryChange = (event) => {
        setSelectedCategory(event.target.value);
        navigate(`/products?category=${event.target.value}`, { replace: true });
    };

    const handleGenderChange = (event) => {
        setSelectedGender(event.target.value);
    };

    const handleColorChange = (event) => {
        setSelectedColor(event.target.value);
    };

    const handleSizeChange = (event) => {
        setSelectedSize(event.target.value);
    };

    const handlePriceRangeChange = (event) => {
        setSelectedPriceRange(event.target.value);
    };

    const handleSortChange = (event) => {
        setSortOption(event.target.value);
    };

    const handleResetFilters = () => {
        setSelectedCategory('');
        setSelectedGender('');
        setSelectedColor('');
        setSelectedSize('');
        setSelectedPriceRange('');
        setSearchTerm('');
        navigate('/products', { replace: true });
    };

return (
    <div className="main-container product-listing-container">
        <div className="filter-container">
            <h2>Filter:</h2>
            <select value={selectedCategory} onChange={handleCategoryChange}>
                <option value="">Kategorie</option>
                {categories.map((category, index) => (
                    <option key={index} value={category}>{category}</option>
                ))}
            </select>
            <select value={selectedGender} onChange={handleGenderChange}>
                <option value="">Geschlecht</option>
                {genders.map((gender, index) => (
                    <option key={index} value={gender}>{gender}</option>
                ))}
            </select>
            <select value={selectedColor} onChange={handleColorChange}>
                <option value="">Farbe</option>
                {colors.map((color, index) => (
                    <option key={index} value={color}>{color}</option>
                ))}
            </select>
            <select value={selectedSize} onChange={handleSizeChange}>
                <option value="">Größe</option>
                {sizes.map((size, index) => (
                    <option key={index} value={size}>{size}</option>
                ))}
            </select>
            <select value={selectedPriceRange} onChange={handlePriceRangeChange}>
                <option value="">Preis</option>
                <option value="0-19">0 - 19 €</option>
                <option value="20-39">20 - 39 €</option>
                <option value="40-59">40 - 59 €</option>
                <option value="60-79">60 - 79 €</option>
                <option value="80-99">80 - 99 €</option>
                <option value="100-9999">100 € und höher</option>
            </select>
            <select value={sortOption} onChange={handleSortChange}>
                <option value="">Sortieren</option>
                <option value="name-asc">Name (A-Z)</option>
                <option value="name-desc">Name (Z-A)</option>
                <option value="price-asc">Preis (aufsteigend)</option>
                <option value="price-desc">Preis (absteigend)</option>
            </select>
            <button onClick={handleResetFilters} className="reset-filters-button">
                Alle Filter zurücksetzen
            </button>
        </div>
        <section className="products-section">
            <div style={{padding: '20px'}}>
                <Title id='list-title' level={1}>{selectedCategory === "" ? "UNSERE PRODUKTE" : `UNSERE ${selectedCategory}`}</Title>
                {filteredProducts.length > 0 ? (
                    <div className="product-list">
                        {filteredProducts.map((product, index) => (
                            <Link to={`/product/${product.id}`} className="product-item" key={index}>
                                <img src={product.imageLink} alt={product.name} className="product-image"/>
                                <div className='flex-column product-detail-wrapper'>
                                    <h3>{product.name}</h3>
                                    {product.amount === 0 && <p style={{ color: 'red' }}>Leider nicht auf Lager</p>}
                                    {product.amount <= 5 && product.amount > 0 && <p style={{ color: 'red' }}>Fast ausverkauft!</p>}
                                    <p id='price'>{product.price.toFixed(2).replace('.', ',')} €</p>
                                </div>
                            </Link>
                        ))}
                    </div>
                ) : (
                    <div className='keine-produkte'>Keine Produkte noch!</div>
                )}
            </div>
        </section>
    </div>
);
};

export default ProductListing;
