import React from 'react';
import type {Policy} from '../types/Policy';

interface PolicyDetailProps {
    policy: Policy;
}

const PolicyDetail: React.FC<PolicyDetailProps> = ({policy}) => {
    return (
        <div>
            <h2>Détail de la police d'assurance</h2>
            <table>
                <tbody>
                <tr>
                    <td><strong>ID</strong></td>
                    <td>{policy.id}</td>
                </tr>
                <tr>
                    <td><strong>Nom</strong></td>
                    <td>{policy.name}</td>
                </tr>
                <tr>
                    <td><strong>Statut</strong></td>
                    <td>{policy.status}</td>
                </tr>
                <tr>
                    <td><strong>Début de couverture</strong></td>
                    <td>{policy.coverageStartDate}</td>
                </tr>
                <tr>
                    <td><strong>Fin de couverture</strong></td>
                    <td>{policy.coverageEndDate}</td>
                </tr>
                <tr>
                    <td><strong>Date de création</strong></td>
                    <td>{policy.creationDate}</td>
                </tr>
                <tr>
                    <td><strong>Date de mise à jour</strong></td>
                    <td>{policy.updateDate}</td>
                </tr>
                </tbody>
            </table>
        </div>
    );
};

export default PolicyDetail;
