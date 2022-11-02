import { IDemandeReparations } from 'app/entities/demande-reparations/demande-reparations.model';

export interface ITypePanne {
  id?: number;
  libelleTypePanne?: string;
  statutSup?: boolean | null;
  demandeReparations?: IDemandeReparations | null;
}

export class TypePanne implements ITypePanne {
  constructor(
    public id?: number,
    public libelleTypePanne?: string,
    public statutSup?: boolean | null,
    public demandeReparations?: IDemandeReparations | null
  ) {
    this.statutSup = this.statutSup ?? false;
  }
}

export function getTypePanneIdentifier(typePanne: ITypePanne): number | undefined {
  return typePanne.id;
}
