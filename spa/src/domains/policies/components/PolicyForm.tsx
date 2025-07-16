import React, {useState, useEffect} from 'react';
import type {Policy, PolicyStatus} from '../types/Policy';

interface PolicyFormProps {
    initialPolicy?: Policy;
    onSubmit: (policy: Partial<Policy>) => void;
    loading?: boolean;
}

const PolicyForm: React.FC<PolicyFormProps> = ({initialPolicy, onSubmit, loading = false}) => {
    const [name, setName] = useState(initialPolicy?.name || '');
    const [status, setStatus] = useState<PolicyStatus>(initialPolicy?.status || 'ACTIVE');
    const [coverageStartDate, setCoverageStartDate] = useState(initialPolicy?.coverageStartDate || '');
    const [coverageEndDate, setCoverageEndDate] = useState(initialPolicy?.coverageEndDate || '');
    const [formError, setFormError] = useState<string | null>(null);

    useEffect(() => {
        setName(initialPolicy?.name || '');
        setStatus(initialPolicy?.status || 'ACTIVE');
        setCoverageStartDate(initialPolicy?.coverageStartDate || '');
        setCoverageEndDate(initialPolicy?.coverageEndDate || '');
    }, [initialPolicy]);

    const validate = (): boolean => {
        if (!name.trim()) {
            setFormError('Policy name is required.');
            return false;
        }
        if (!coverageStartDate) {
            setFormError('Coverage start date is required.');
            return false;
        }
        if (!coverageEndDate) {
            setFormError('Coverage end date is required.');
            return false;
        }
        setFormError(null);
        return true;
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (!validate()) return;
        onSubmit({
            name,
            status,
            coverageStartDate,
            coverageEndDate,
        });
    };

    return (
        <form onSubmit={handleSubmit} style={{
            display: 'flex',
            flexDirection: 'column',
            gap: '1.5rem',
            maxWidth: '500px',
            margin: '2rem auto',
            background: '#fff',
            borderRadius: '12px',
            boxShadow: '0 2px 16px rgba(25,118,210,0.08)',
            padding: '2rem'
        }}>
            {initialPolicy && (
                <div style={{display: 'flex', flexDirection: 'column', gap: '0.5rem'}}>
                    <label htmlFor="id" style={{fontWeight: 600}}>ID</label>
                    <input id="id" type="text" value={initialPolicy.id} disabled style={{
                        padding: '0.5rem',
                        borderRadius: '6px',
                        border: '1px solid #bdbdbd',
                        background: '#f5f5f5'
                    }}/>
                </div>
            )}
            <div style={{display: 'flex', flexDirection: 'column', gap: '0.5rem'}}>
                <label htmlFor="name" style={{fontWeight: 600}}>Name</label>
                <input id="name" name="name" value={name} onChange={e => setName(e.target.value)} required
                       style={{padding: '0.5rem', borderRadius: '6px', border: '1px solid #bdbdbd'}}/>
            </div>
            <div style={{display: 'flex', flexDirection: 'column', gap: '0.5rem'}}>
                <label htmlFor="status" style={{fontWeight: 600}}>Status</label>
                <select id="status" name="status" value={status}
                        onChange={e => setStatus(e.target.value as PolicyStatus)} required
                        style={{padding: '0.5rem', borderRadius: '6px', border: '1px solid #bdbdbd'}}>
                    <option value="ACTIVE">ACTIVE</option>
                    <option value="INACTIVE">INACTIVE</option>
                </select>
            </div>
            <div style={{display: 'flex', flexDirection: 'column', gap: '0.5rem'}}>
                <label htmlFor="coverageStartDate" style={{fontWeight: 600}}>Start Date</label>
                <input id="coverageStartDate" name="coverageStartDate" type="date" value={coverageStartDate}
                       onChange={e => setCoverageStartDate(e.target.value)} required
                       style={{padding: '0.5rem', borderRadius: '6px', border: '1px solid #bdbdbd'}}/>
            </div>
            <div style={{display: 'flex', flexDirection: 'column', gap: '0.5rem'}}>
                <label htmlFor="coverageEndDate" style={{fontWeight: 600}}>End Date</label>
                <input id="coverageEndDate" name="coverageEndDate" type="date" value={coverageEndDate}
                       onChange={e => setCoverageEndDate(e.target.value)} required
                       style={{padding: '0.5rem', borderRadius: '6px', border: '1px solid #bdbdbd'}}/>
            </div>
            {formError && (
                <div style={{
                    color: '#d32f2f',
                    textAlign: 'center',
                    fontWeight: 600,
                    marginBottom: '1rem'
                }}>{formError}</div>
            )}
            <div style={{display: 'flex', gap: '1rem', justifyContent: 'center', marginTop: '1.5rem'}}>
                <button type="submit" disabled={loading} style={{
                    padding: '0.7rem 2rem',
                    fontSize: '1.1rem',
                    background: 'linear-gradient(90deg, #1976d2 60%, #42a5f5 100%)',
                    color: '#fff',
                    border: 'none',
                    borderRadius: '24px',
                    cursor: 'pointer',
                    fontWeight: 600
                }}>
                    {initialPolicy ? 'Enregistrer' : 'Créer'}
                </button>
                <button type="button"
                        onClick={() => window.history.length > 1 ? window.history.back() : window.location.assign('/policies')}
                        style={{
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
        </form>
    );
};

export default PolicyForm;
