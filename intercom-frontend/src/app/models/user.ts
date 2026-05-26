import { Apartment } from "./apartment";

export interface User {
    id: number,
    username: string,
    password: string,
    active: boolean,
    apartment: Apartment | null
}
