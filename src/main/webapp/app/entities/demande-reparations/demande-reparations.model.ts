import dayjs from 'dayjs/esm';
import { ITypeMatiere } from 'app/entities/type-matiere/type-matiere.model';
import { IStructure } from 'app/entities/structure/structure.model';

export interface IDemandeReparations {
  id?: number;
  date?: dayjs.Dayjs;
  indentiteSoumettant?: string;
  fonction?: string;
  designation?: string;
  observation?: string | null;
  statutSup?: boolean | null;
  typeMatieres?: ITypeMatiere[] | null;
  structure?: IStructure | null;
}

export class DemandeReparations implements IDemandeReparations {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs,
    public indentiteSoumettant?: string,
    public fonction?: string,
    public designation?: string,
    public observation?: string | null,
    public statutSup?: boolean | null,
    public typeMatieres?: ITypeMatiere[] | null,
    public structure?: IStructure | null
  ) {
    this.statutSup = this.statutSup ?? false;
  }
}

export function getDemandeReparationsIdentifier(demandeReparations: IDemandeReparations): number | undefined {
  return demandeReparations.id;
}
