import { Sector } from "./sector";

export interface Form {
    id: number,
    name: string,
    sectors: Sector[],
    hasAgreed: boolean
}