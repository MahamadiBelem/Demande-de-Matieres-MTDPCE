import { ILivraisonMatieres } from 'app/entities/livraison-matieres/livraison-matieres.model';

export interface IMatieres {
  id?: number;
  designationMatieres?: string;
  quantiteMatieres?: number;
  caracteristiquesMatieres?: string | null;
  statutSup?: boolean | null;
  livraisonMatieres?: ILivraisonMatieres[] | null;
}

export class Matieres implements IMatieres {
  constructor(
    public id?: number,
    public designationMatieres?: string,
    public quantiteMatieres?: number,
    public caracteristiquesMatieres?: string | null,
    public statutSup?: boolean | null,
    public livraisonMatieres?: ILivraisonMatieres[] | null
  ) {
    this.statutSup = this.statutSup ?? false;
  }
}

export function getMatieresIdentifier(matieres: IMatieres): number | undefined {
  return matieres.id;
}
