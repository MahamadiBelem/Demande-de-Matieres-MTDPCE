import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDemandeReparations, getDemandeReparationsIdentifier } from '../demande-reparations.model';

export type EntityResponseType = HttpResponse<IDemandeReparations>;
export type EntityArrayResponseType = HttpResponse<IDemandeReparations[]>;

@Injectable({ providedIn: 'root' })
export class DemandeReparationsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/demande-reparations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(demandeReparations: IDemandeReparations): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeReparations);
    return this.http
      .post<IDemandeReparations>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(demandeReparations: IDemandeReparations): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeReparations);
    return this.http
      .put<IDemandeReparations>(`${this.resourceUrl}/${getDemandeReparationsIdentifier(demandeReparations) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(demandeReparations: IDemandeReparations): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeReparations);
    return this.http
      .patch<IDemandeReparations>(`${this.resourceUrl}/${getDemandeReparationsIdentifier(demandeReparations) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDemandeReparations>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDemandeReparations[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDemandeReparationsToCollectionIfMissing(
    demandeReparationsCollection: IDemandeReparations[],
    ...demandeReparationsToCheck: (IDemandeReparations | null | undefined)[]
  ): IDemandeReparations[] {
    const demandeReparations: IDemandeReparations[] = demandeReparationsToCheck.filter(isPresent);
    if (demandeReparations.length > 0) {
      const demandeReparationsCollectionIdentifiers = demandeReparationsCollection.map(
        demandeReparationsItem => getDemandeReparationsIdentifier(demandeReparationsItem)!
      );
      const demandeReparationsToAdd = demandeReparations.filter(demandeReparationsItem => {
        const demandeReparationsIdentifier = getDemandeReparationsIdentifier(demandeReparationsItem);
        if (demandeReparationsIdentifier == null || demandeReparationsCollectionIdentifiers.includes(demandeReparationsIdentifier)) {
          return false;
        }
        demandeReparationsCollectionIdentifiers.push(demandeReparationsIdentifier);
        return true;
      });
      return [...demandeReparationsToAdd, ...demandeReparationsCollection];
    }
    return demandeReparationsCollection;
  }

  protected convertDateFromClient(demandeReparations: IDemandeReparations): IDemandeReparations {
    return Object.assign({}, demandeReparations, {
      date: demandeReparations.date?.isValid() ? demandeReparations.date.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((demandeReparations: IDemandeReparations) => {
        demandeReparations.date = demandeReparations.date ? dayjs(demandeReparations.date) : undefined;
      });
    }
    return res;
  }
}
