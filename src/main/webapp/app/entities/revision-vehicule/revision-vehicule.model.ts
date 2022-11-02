import { ICarnetVehicule } from 'app/entities/carnet-vehicule/carnet-vehicule.model';

export interface IRevisionVehicule {
  id?: number;
  libelleRevisionVehicule?: string;
  carnetVehicule?: ICarnetVehicule | null;
}

export class RevisionVehicule implements IRevisionVehicule {
  constructor(public id?: number, public libelleRevisionVehicule?: string, public carnetVehicule?: ICarnetVehicule | null) {}
}

export function getRevisionVehiculeIdentifier(revisionVehicule: IRevisionVehicule): number | undefined {
  return revisionVehicule.id;
}
