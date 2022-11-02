import dayjs from 'dayjs/esm';
import { IMarqueVehicule } from 'app/entities/marque-vehicule/marque-vehicule.model';
import { IStructure } from 'app/entities/structure/structure.model';
import { Etatvehicule } from 'app/entities/enumerations/etatvehicule.model';

export interface ICarnetVehicule {
  id?: number;
  date?: dayjs.Dayjs;
  immatriculationVehicule?: string;
  identiteConducteur?: string;
  nombreReparation?: number | null;
  dateDerniereRevision?: dayjs.Dayjs | null;
  etatvehicule?: Etatvehicule | null;
  observations?: string | null;
  statutSup?: boolean | null;
  marqueVehicule?: IMarqueVehicule | null;
  structures?: IStructure[] | null;
}

export class CarnetVehicule implements ICarnetVehicule {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs,
    public immatriculationVehicule?: string,
    public identiteConducteur?: string,
    public nombreReparation?: number | null,
    public dateDerniereRevision?: dayjs.Dayjs | null,
    public etatvehicule?: Etatvehicule | null,
    public observations?: string | null,
    public statutSup?: boolean | null,
    public marqueVehicule?: IMarqueVehicule | null,
    public structures?: IStructure[] | null
  ) {
    this.statutSup = this.statutSup ?? false;
  }
}

export function getCarnetVehiculeIdentifier(carnetVehicule: ICarnetVehicule): number | undefined {
  return carnetVehicule.id;
}
