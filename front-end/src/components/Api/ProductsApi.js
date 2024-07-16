import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:1234/product',
});

const ProductsApi = {
    getProducts: async () => {
        try {
            const response = await apiClient.get('/products');
            return response; // returning actual response
        } catch (error) {
            console.error('Error fetching products:', error);
            throw error;
        }
    },

    getProduct: async (id) => {
        try {
            const response = await apiClient.get(`/${id}`);
            return response;
        } catch (error) {
            console.error(`Error fetching product with id ${id}:`, error);
            throw error;
        }
    },

    createProduct: async (product) => {
        try {
            const response = await apiClient.post('/', product);
            return response;
        } catch (error) {
            console.error('Error creating product:', error);
            throw error;
        }
    },

    updateProduct: async (id, product) => {
        try {
            const response = await apiClient.put(`/admin/${id}`, product);
            return response;
        } catch (error) {
            console.error(`Error updating product with id ${id}:`, error);
            throw error;
        }
    },

    deleteProduct: async (id) => {
        try {
            const response = await apiClient.delete(`/admin/${id}`);
            return response;
        } catch (error) {
            console.error(`Error deleting product with id ${id}:`, error);
            throw error;
        }
    }
};

export default ProductsApi;
