export type PolicyStatus = 'ACTIVE' | 'INACTIVE';

export interface Policy {
    id: number;
    name: string;
    status: PolicyStatus;
    coverageStartDate: string;
    coverageEndDate: string;
    creationDate: string;
    updateDate: string;
}

