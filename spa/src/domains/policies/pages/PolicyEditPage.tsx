import React, {useEffect, useState} from 'react';
import {useParams, useNavigate} from 'react-router-dom';
import {getPolicy, createPolicy, updatePolicy} from '../api/policyApi';
import type {Policy} from '../types/Policy';
import PolicyForm from '../components/PolicyForm';

const PolicyEditPage: React.FC = () => {
    const {id} = useParams<{ id?: string }>();
    const navigate = useNavigate();
    const [initialPolicy, setInitialPolicy] = useState<Policy | undefined>(undefined);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (id) {
            setLoading(true);
            getPolicy(Number(id))
                .then(setInitialPolicy)
                .catch(() => setError('Erreur lors du chargement de la police.'))
                .finally(() => setLoading(false));
        }
    }, [id]);

    const handleSubmit = async (policyData: Partial<Policy>) => {
        setLoading(true);
        setError(null);
        try {
            if (id && initialPolicy) {
                await updatePolicy(Number(id), policyData);
                navigate(`/policies/${id}`);
            } else {
                const created = await createPolicy(policyData as any);
                navigate(`/policies/${created.id}`);
            }
        } catch (e) {
            setError('Erreur lors de la sauvegarde de la police.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{
            background: 'linear-gradient(90deg, #e3f2fd 0%, #fff 100%)',
            minHeight: '100vh',
            paddingBottom: '2rem'
        }}>
            <header style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                marginBottom: '2rem',
                paddingTop: '2rem'
            }}>
                <img
                    src="https://images.teamtailor-cdn.com/images/s3/teamtailor-production/logotype-v3/image_uploads/e635bbdb-1e15-46b8-b5e1-24bc4a4a15e8/original.png"
                    alt="Logo" style={{
                    maxWidth: '120px',
                    marginRight: '1.5rem',
                    boxShadow: '0 2px 8px rgba(0,0,0,0.08)',
                    borderRadius: '8px'
                }}/>
                <h1 style={{
                    margin: 0,
                    fontSize: '2.2rem',
                    color: '#1976d2',
                    fontWeight: 700,
                    letterSpacing: '1px',
                    textShadow: '0 1px 2px #fff'
                }}>{id ? "Édition de la police d'assurance" : "Création d'une police d'assurance"}</h1>
            </header>
            <div style={{
                maxWidth: '600px',
                margin: '0 auto',
                background: 'transparent',
                borderRadius: '12px',
                boxShadow: 'none',
                padding: '2rem'
            }}>
                {loading && <div>Loading...</div>}
                {error && <div>{error}</div>}
                {!loading && (
                    <PolicyForm initialPolicy={initialPolicy} onSubmit={handleSubmit}/>
                )}
            </div>
        </div>
    );
};

export default PolicyEditPage;
