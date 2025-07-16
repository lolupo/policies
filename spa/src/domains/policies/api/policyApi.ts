import axios from 'axios';
import type {Policy} from '../types/Policy';

const API_BASE_URL = '/api/policies';

export interface PaginatedResponse<T> {
    content: T[];
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
}

export const getPolicies = async (page = 0, size = 10): Promise<PaginatedResponse<Policy>> => {
    const response = await axios.get(API_BASE_URL, {
        params: {page, size}
    });
    return response.data;
};

export const getPolicy = async (id: number): Promise<Policy> => {
    const response = await axios.get(`${API_BASE_URL}/${id}`);
    return response.data;
};

export const createPolicy = async (policy: Omit<Policy, 'id' | 'creationDate' | 'updateDate'>): Promise<Policy> => {
    const response = await axios.post(API_BASE_URL, policy);
    return response.data;
};

export const updatePolicy = async (id: number, policy: Partial<Policy>): Promise<Policy> => {
    const response = await axios.put(`${API_BASE_URL}/${id}`, policy);
    return response.data;
};
