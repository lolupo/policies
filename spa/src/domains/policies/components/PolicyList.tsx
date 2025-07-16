import React, {useEffect, useState} from 'react';
import type {PaginatedResponse} from '../api/policyApi';
import {getPolicies} from '../api/policyApi';
import type {Policy} from '../types/Policy';
import {useNavigate} from 'react-router-dom';

const PAGE_SIZE = 10;

const PolicyList: React.FC = () => {
    const [data, setData] = useState<PaginatedResponse<Policy> | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [page, setPage] = useState<number>(0);
    const navigate = useNavigate();

    useEffect(() => {
        setLoading(true);
        getPolicies(page, PAGE_SIZE)
            .then(setData)
            .catch(() => setError('Error loading policies.'))
            .finally(() => setLoading(false));
    }, [page]);


    if (loading) return <div>Loading...</div>;
    if (error) return <div>{error}</div>;
    if (!data) return <div>No data.</div>;

    return (
        <div>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Status</th>
                    <th>Start</th>
                    <th>End</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {data.content.map(policy => (
                    <tr key={policy.id}>
                        <td>{policy.id}</td>
                        <td>{policy.name}</td>
                        <td>{policy.status}</td>
                        <td>{policy.coverageStartDate}</td>
                        <td>{policy.coverageEndDate}</td>
                        <td>
                            <button onClick={() => navigate(`/policies/${policy.id}/edit`)}>
                                Edit
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <div style={{marginTop: '1em', display: 'flex', justifyContent: 'center', gap: '1em'}}>
                <button onClick={() => setPage(page - 1)} disabled={page === 0}>Previous</button>
                <span>Page {data.page + 1} of {data.totalPages}</span>
                <button onClick={() => setPage(page + 1)} disabled={page + 1 >= data.totalPages}>Next</button>
            </div>
        </div>
    );
};

export default PolicyList;
