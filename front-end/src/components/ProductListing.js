import React, { useState, useEffect } from 'react';
import {useLocation, useNavigate} from "react-router-dom";
import ProductsApi from './Api/ProductsApi';
import '../ProductListing.css';

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
    const query = useQuery();
    const navigate = useNavigate();

    useEffect(() => {
        const handleProducts = async () => {
            try {
                const response = await ProductsApi.getProducts();
                setProducts(response.data);
                setFilteredProducts(response.data);
                const uniqueCategories = [...new Set(response.data.map(product => product.category))];
                const uniqueGenders = [...new Set(response.data.map(product => product.gender))];
                const uniqueColors = [...new Set(response.data.map(product => product.color))];
                const uniqueSizes = [...new Set(response.data.map(product => product.size))];
                setCategories(uniqueCategories);
                setGenders(uniqueGenders);
                setColors(uniqueColors);
                setSizes(uniqueSizes);

                const categoryFromUrl = query.get('category');
                if (categoryFromUrl) {
                    setSelectedCategory(categoryFromUrl);
                    navigate('/products', { replace: true });
                }

                const searchFromUrl = query.get('search');
                if (searchFromUrl) {
                    setSearchTerm(searchFromUrl);
                }
            } catch (error) {
                console.error('Error fetching products:', error);
            }
        };
        handleProducts();
    }, [query, navigate]);

    const handleCategoryChange = (event) => {
        setSelectedCategory(event.target.value);
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

    const handleResetFilters = () => {
        window.location.href = '/products';
    };

    useEffect(() => {
        let filtered = products;

        if (selectedCategory) {
            filtered = products.filter(product => product.category === selectedCategory);
        }

        if (selectedGender) {
            filtered = products.filter(product => product.gender === selectedGender);
        }

        if (selectedColor) {
            filtered = products.filter(product => product.color === selectedColor);
        }

        if (selectedSize) {
            filtered = products.filter(product => product.size === selectedSize);
        }

        if (selectedPriceRange) {
            const [min, max] = selectedPriceRange.split('-').map(Number);
            filtered = products.filter(product => product.price >= min && product.price <= max);
        }

        if (searchTerm) {
            filtered = filtered.filter(product => product.title.toLowerCase().includes(searchTerm.toLowerCase()));
        }

        setFilteredProducts(filtered);
    }, [selectedCategory, selectedGender, selectedColor, selectedSize, selectedPriceRange, products, query, searchTerm]);

    return (
        <div className="product-listing-container">
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
                <button onClick={handleResetFilters} className="reset-filters-button">
                    Alle Filter zurücksetzen
                </button>
            </div>
            <div className="product-grid">
                {filteredProducts.map((product, index) => (
                    <div className="product-item" key={index}>
                        <img src={product.foto} alt={product.title} className="product-image" />
                        <h3>{product.title}</h3>
                        <p>{product.description}</p>
                        <p>{product.price} €</p>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ProductListing;
