import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDemandeMatieres, getDemandeMatieresIdentifier } from '../demande-matieres.model';

export type EntityResponseType = HttpResponse<IDemandeMatieres>;
export type EntityArrayResponseType = HttpResponse<IDemandeMatieres[]>;

@Injectable({ providedIn: 'root' })
export class DemandeMatieresService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/demande-matieres');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(demandeMatieres: IDemandeMatieres): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeMatieres);
    return this.http
      .post<IDemandeMatieres>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(demandeMatieres: IDemandeMatieres): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeMatieres);
    return this.http
      .put<IDemandeMatieres>(`${this.resourceUrl}/${getDemandeMatieresIdentifier(demandeMatieres) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(demandeMatieres: IDemandeMatieres): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeMatieres);
    return this.http
      .patch<IDemandeMatieres>(`${this.resourceUrl}/${getDemandeMatieresIdentifier(demandeMatieres) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDemandeMatieres>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDemandeMatieres[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDemandeMatieresToCollectionIfMissing(
    demandeMatieresCollection: IDemandeMatieres[],
    ...demandeMatieresToCheck: (IDemandeMatieres | null | undefined)[]
  ): IDemandeMatieres[] {
    const demandeMatieres: IDemandeMatieres[] = demandeMatieresToCheck.filter(isPresent);
    if (demandeMatieres.length > 0) {
      const demandeMatieresCollectionIdentifiers = demandeMatieresCollection.map(
        demandeMatieresItem => getDemandeMatieresIdentifier(demandeMatieresItem)!
      );
      const demandeMatieresToAdd = demandeMatieres.filter(demandeMatieresItem => {
        const demandeMatieresIdentifier = getDemandeMatieresIdentifier(demandeMatieresItem);
        if (demandeMatieresIdentifier == null || demandeMatieresCollectionIdentifiers.includes(demandeMatieresIdentifier)) {
          return false;
        }
        demandeMatieresCollectionIdentifiers.push(demandeMatieresIdentifier);
        return true;
      });
      return [...demandeMatieresToAdd, ...demandeMatieresCollection];
    }
    return demandeMatieresCollection;
  }

  protected convertDateFromClient(demandeMatieres: IDemandeMatieres): IDemandeMatieres {
    return Object.assign({}, demandeMatieres, {
      date: demandeMatieres.date?.isValid() ? demandeMatieres.date.toJSON() : undefined,
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
      res.body.forEach((demandeMatieres: IDemandeMatieres) => {
        demandeMatieres.date = demandeMatieres.date ? dayjs(demandeMatieres.date) : undefined;
      });
    }
    return res;
  }
}
