import dayjs from 'dayjs/esm';
import { IStructure } from 'app/entities/structure/structure.model';

export interface IDemandeMatieres {
  id?: number;
  date?: dayjs.Dayjs;
  indentiteSoumettant?: string;
  fonction?: string;
  designation?: string;
  quantite?: number;
  observation?: string | null;
  statutSup?: boolean | null;
  structure?: IStructure | null;
}

export class DemandeMatieres implements IDemandeMatieres {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs,
    public indentiteSoumettant?: string,
    public fonction?: string,
    public designation?: string,
    public quantite?: number,
    public observation?: string | null,
    public statutSup?: boolean | null,
    public structure?: IStructure | null
  ) {
    this.statutSup = this.statutSup ?? false;
  }
}

export function getDemandeMatieresIdentifier(demandeMatieres: IDemandeMatieres): number | undefined {
  return demandeMatieres.id;
}
