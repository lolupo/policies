import React, {useEffect, useState} from 'react';
import {useParams} from 'react-router-dom';
import {getPolicy} from '../api/policyApi';
import type {Policy} from '../types/Policy';
import PolicyDetail from '../components/PolicyDetail';

const PolicyDetailPage: React.FC = () => {
    const {id} = useParams<{ id: string }>();
    const [policy, setPolicy] = useState<Policy | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!id) return;
        getPolicy(Number(id))
            .then(setPolicy)
            .catch(() => setError('Erreur lors du chargement de la police.'))
            .finally(() => setLoading(false));
    }, [id]);

    if (loading) return <div>Chargement...</div>;
    if (error) return <div>{error}</div>;
    if (!policy) return <div>Aucune police trouvée.</div>;

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
                }}>Détail de la police d'assurance</h1>
            </header>
            <div style={{
                maxWidth: '600px',
                margin: '0 auto',
                background: 'transparent',
                borderRadius: '12px',
                boxShadow: 'none',
                padding: '2rem'
            }}>
                <PolicyDetail policy={policy}/>
                <div style={{display: 'flex', justifyContent: 'center', marginTop: '2rem'}}>
                    <button onClick={() => window.location.assign('/policies/dashboard')} style={{
                        padding: '0.7rem 2rem',
                        fontSize: '1.1rem',
                        background: '#bdbdbd',
                        color: '#fff',
                        border: 'none',
                        borderRadius: '24px',
                        cursor: 'pointer',
                        fontWeight: 600
                    }}>Retour
                    </button>
                </div>
            </div>
        </div>
    );
};

export default PolicyDetailPage;
