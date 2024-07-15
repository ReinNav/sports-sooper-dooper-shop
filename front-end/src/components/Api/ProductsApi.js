import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:1234/api',
});

const exProductList = [
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

const ProductsApi = {

    getProducts: async () => {
        try {
            //const response = await apiClient.get('/products', {});
            return { data: exProductList };
        } catch (error) {
            console.error('Error fetching products:', error);
            throw error;
        }
    }
};

export default ProductsApi;
