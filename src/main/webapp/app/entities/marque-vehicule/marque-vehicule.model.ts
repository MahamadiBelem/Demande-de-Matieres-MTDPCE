import { ICarnetVehicule } from 'app/entities/carnet-vehicule/carnet-vehicule.model';

export interface IMarqueVehicule {
  id?: number;
  libelleMarqueVehicule?: string;
  carnetVehicule?: ICarnetVehicule | null;
}

export class MarqueVehicule implements IMarqueVehicule {
  constructor(public id?: number, public libelleMarqueVehicule?: string, public carnetVehicule?: ICarnetVehicule | null) {}
}

export function getMarqueVehiculeIdentifier(marqueVehicule: IMarqueVehicule): number | undefined {
  return marqueVehicule.id;
}
