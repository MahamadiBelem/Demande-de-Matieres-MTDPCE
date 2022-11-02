import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICarnetVehicule, getCarnetVehiculeIdentifier } from '../carnet-vehicule.model';

export type EntityResponseType = HttpResponse<ICarnetVehicule>;
export type EntityArrayResponseType = HttpResponse<ICarnetVehicule[]>;

@Injectable({ providedIn: 'root' })
export class CarnetVehiculeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/carnet-vehicules');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(carnetVehicule: ICarnetVehicule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(carnetVehicule);
    return this.http
      .post<ICarnetVehicule>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(carnetVehicule: ICarnetVehicule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(carnetVehicule);
    return this.http
      .put<ICarnetVehicule>(`${this.resourceUrl}/${getCarnetVehiculeIdentifier(carnetVehicule) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(carnetVehicule: ICarnetVehicule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(carnetVehicule);
    return this.http
      .patch<ICarnetVehicule>(`${this.resourceUrl}/${getCarnetVehiculeIdentifier(carnetVehicule) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICarnetVehicule>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICarnetVehicule[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCarnetVehiculeToCollectionIfMissing(
    carnetVehiculeCollection: ICarnetVehicule[],
    ...carnetVehiculesToCheck: (ICarnetVehicule | null | undefined)[]
  ): ICarnetVehicule[] {
    const carnetVehicules: ICarnetVehicule[] = carnetVehiculesToCheck.filter(isPresent);
    if (carnetVehicules.length > 0) {
      const carnetVehiculeCollectionIdentifiers = carnetVehiculeCollection.map(
        carnetVehiculeItem => getCarnetVehiculeIdentifier(carnetVehiculeItem)!
      );
      const carnetVehiculesToAdd = carnetVehicules.filter(carnetVehiculeItem => {
        const carnetVehiculeIdentifier = getCarnetVehiculeIdentifier(carnetVehiculeItem);
        if (carnetVehiculeIdentifier == null || carnetVehiculeCollectionIdentifiers.includes(carnetVehiculeIdentifier)) {
          return false;
        }
        carnetVehiculeCollectionIdentifiers.push(carnetVehiculeIdentifier);
        return true;
      });
      return [...carnetVehiculesToAdd, ...carnetVehiculeCollection];
    }
    return carnetVehiculeCollection;
  }

  protected convertDateFromClient(carnetVehicule: ICarnetVehicule): ICarnetVehicule {
    return Object.assign({}, carnetVehicule, {
      date: carnetVehicule.date?.isValid() ? carnetVehicule.date.toJSON() : undefined,
      dateDerniereRevision: carnetVehicule.dateDerniereRevision?.isValid()
        ? carnetVehicule.dateDerniereRevision.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
      res.body.dateDerniereRevision = res.body.dateDerniereRevision ? dayjs(res.body.dateDerniereRevision) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((carnetVehicule: ICarnetVehicule) => {
        carnetVehicule.date = carnetVehicule.date ? dayjs(carnetVehicule.date) : undefined;
        carnetVehicule.dateDerniereRevision = carnetVehicule.dateDerniereRevision ? dayjs(carnetVehicule.dateDerniereRevision) : undefined;
      });
    }
    return res;
  }
}
