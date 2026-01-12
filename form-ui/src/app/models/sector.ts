export interface Sector {
  id: number;
  name: string;
  children?: Sector[];
}

export interface FlatSector extends Sector {
  level: number;
}
