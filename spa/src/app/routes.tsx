import {BrowserRouter, Routes, Route, Navigate} from 'react-router-dom';
import PoliciesPage from '../domains/policies/pages/PoliciesPage';
import PolicyDetailPage from '../domains/policies/pages/PolicyDetailPage';
import PolicyEditPage from '../domains/policies/pages/PolicyEditPage';

const AppRoutes = () => (
    <BrowserRouter>
        <Routes>
            <Route path="/policies/dashboard" element={<PoliciesPage/>}/>
            <Route path="/policies" element={<PoliciesPage/>}/>
            <Route path="/policies/:id" element={<PolicyDetailPage/>}/>
            <Route path="/policies/:id/edit" element={<PolicyEditPage/>}/>
            <Route path="/policies/create" element={<PolicyEditPage/>}/>
            <Route path="*" element={<Navigate to="/policies/dashboard" replace/>}/>
        </Routes>
    </BrowserRouter>
);

export default AppRoutes;
