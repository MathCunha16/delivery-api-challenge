import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api/v1',
});

// Add a response interceptor to handle errors globally if needed
api.interceptors.response.use(
    (response) => response,
    (error) => {
        // We can add global error handling here later, e.g., toast notifications
        return Promise.reject(error);
    }
);

export default api;
