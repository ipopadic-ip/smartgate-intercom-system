import { User } from "./user";

export interface ActionLog {

    id: number;
    action: string;
    timestamp: string; 
    user: User;
    doorNumber: number;
    active: boolean;
}
