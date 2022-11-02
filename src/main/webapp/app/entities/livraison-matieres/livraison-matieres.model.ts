import dayjs from 'dayjs/esm';
import { IMatieres } from 'app/entities/matieres/matieres.model';
import { IStructure } from 'app/entities/structure/structure.model';

export interface ILivraisonMatieres {
  id?: number;
  designationMatiere?: string;
  quantiteLivree?: number;
  dateLivree?: dayjs.Dayjs | null;
  statutSup?: boolean | null;
  matieres?: IMatieres[] | null;
  structure?: IStructure | null;
}

export class LivraisonMatieres implements ILivraisonMatieres {
  constructor(
    public id?: number,
    public designationMatiere?: string,
    public quantiteLivree?: number,
    public dateLivree?: dayjs.Dayjs | null,
    public statutSup?: boolean | null,
    public matieres?: IMatieres[] | null,
    public structure?: IStructure | null
  ) {
    this.statutSup = this.statutSup ?? false;
  }
}

export function getLivraisonMatieresIdentifier(livraisonMatieres: ILivraisonMatieres): number | undefined {
  return livraisonMatieres.id;
}
