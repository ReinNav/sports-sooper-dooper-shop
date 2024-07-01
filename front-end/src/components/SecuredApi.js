import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:1234/api/secured',
});

const SecuredApi = {
    getSecuredData: async (accessToken) => {
        try {
            const response = await apiClient.get('/test', {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            });
            return response;
        } catch (error) {
            console.error('Error fetching secured data:', error);
            throw error;
        }
    }
};

export default SecuredApi;
