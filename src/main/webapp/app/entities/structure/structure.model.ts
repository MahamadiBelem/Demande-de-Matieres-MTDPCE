import { IDemandeMatieres } from 'app/entities/demande-matieres/demande-matieres.model';
import { IDemandeReparations } from 'app/entities/demande-reparations/demande-reparations.model';
import { ILivraisonMatieres } from 'app/entities/livraison-matieres/livraison-matieres.model';
import { ICarnetVehicule } from 'app/entities/carnet-vehicule/carnet-vehicule.model';

export interface IStructure {
  id?: number;
  libelleStructure?: string;
  codeStructure?: string;
  demandeMatieres?: IDemandeMatieres[] | null;
  demandeReparations?: IDemandeReparations[] | null;
  livraisonMatieres?: ILivraisonMatieres[] | null;
  carnetVehicules?: ICarnetVehicule[] | null;
}

export class Structure implements IStructure {
  constructor(
    public id?: number,
    public libelleStructure?: string,
    public codeStructure?: string,
    public demandeMatieres?: IDemandeMatieres[] | null,
    public demandeReparations?: IDemandeReparations[] | null,
    public livraisonMatieres?: ILivraisonMatieres[] | null,
    public carnetVehicules?: ICarnetVehicule[] | null
  ) {}
}

export function getStructureIdentifier(structure: IStructure): number | undefined {
  return structure.id;
}
