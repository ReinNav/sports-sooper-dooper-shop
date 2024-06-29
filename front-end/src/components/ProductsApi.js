import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:1234/api',
});

const ProductsApi = {
    getProducts: async () => {
        try {
            const response = await apiClient.get('/products', {});
            return response;
        } catch (error) {
            console.error('Error fetching products:', error);
            throw error;
        }
    }
};

export default ProductsApi;
