import { IDemandeReparations } from 'app/entities/demande-reparations/demande-reparations.model';

export interface ITypeMatiere {
  id?: number;
  libelleTypeMatiere?: string;
  demandeReparations?: IDemandeReparations[] | null;
}

export class TypeMatiere implements ITypeMatiere {
  constructor(public id?: number, public libelleTypeMatiere?: string, public demandeReparations?: IDemandeReparations[] | null) {}
}

export function getTypeMatiereIdentifier(typeMatiere: ITypeMatiere): number | undefined {
  return typeMatiere.id;
}
