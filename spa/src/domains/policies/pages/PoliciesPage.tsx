import React from 'react';
import {useNavigate} from 'react-router-dom';
import PolicyList from '../components/PolicyList';

const PoliciesPage: React.FC = () => {
    const navigate = useNavigate();

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
                }}>Gestion des polices d'assurance</h1>
            </header>
            <div style={{
                maxWidth: '900px',
                margin: '0 auto',
                background: 'transparent',
                borderRadius: '12px',
                boxShadow: 'none',
                padding: '2rem'
            }}>
                <PolicyList/>
                <div style={{display: 'flex', justifyContent: 'center', marginTop: '2rem'}}>
                    <button onClick={() => navigate('/policies/create')} style={{
                        padding: '0.7rem 2rem',
                        fontSize: '1.1rem',
                        background: 'linear-gradient(90deg, #1976d2 60%, #42a5f5 100%)',
                        color: '#fff',
                        border: 'none',
                        borderRadius: '24px',
                        cursor: 'pointer',
                        boxShadow: '0 2px 8px rgba(25,118,210,0.15)',
                        fontWeight: 600,
                        transition: 'background 0.2s'
                    }}>
                        Créer une police
                    </button>
                </div>
            </div>
        </div>
    );
};

export default PoliciesPage;
