import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILivraisonMatieres, getLivraisonMatieresIdentifier } from '../livraison-matieres.model';

export type EntityResponseType = HttpResponse<ILivraisonMatieres>;
export type EntityArrayResponseType = HttpResponse<ILivraisonMatieres[]>;

@Injectable({ providedIn: 'root' })
export class LivraisonMatieresService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/livraison-matieres');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(livraisonMatieres: ILivraisonMatieres): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(livraisonMatieres);
    return this.http
      .post<ILivraisonMatieres>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(livraisonMatieres: ILivraisonMatieres): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(livraisonMatieres);
    return this.http
      .put<ILivraisonMatieres>(`${this.resourceUrl}/${getLivraisonMatieresIdentifier(livraisonMatieres) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(livraisonMatieres: ILivraisonMatieres): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(livraisonMatieres);
    return this.http
      .patch<ILivraisonMatieres>(`${this.resourceUrl}/${getLivraisonMatieresIdentifier(livraisonMatieres) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILivraisonMatieres>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILivraisonMatieres[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLivraisonMatieresToCollectionIfMissing(
    livraisonMatieresCollection: ILivraisonMatieres[],
    ...livraisonMatieresToCheck: (ILivraisonMatieres | null | undefined)[]
  ): ILivraisonMatieres[] {
    const livraisonMatieres: ILivraisonMatieres[] = livraisonMatieresToCheck.filter(isPresent);
    if (livraisonMatieres.length > 0) {
      const livraisonMatieresCollectionIdentifiers = livraisonMatieresCollection.map(
        livraisonMatieresItem => getLivraisonMatieresIdentifier(livraisonMatieresItem)!
      );
      const livraisonMatieresToAdd = livraisonMatieres.filter(livraisonMatieresItem => {
        const livraisonMatieresIdentifier = getLivraisonMatieresIdentifier(livraisonMatieresItem);
        if (livraisonMatieresIdentifier == null || livraisonMatieresCollectionIdentifiers.includes(livraisonMatieresIdentifier)) {
          return false;
        }
        livraisonMatieresCollectionIdentifiers.push(livraisonMatieresIdentifier);
        return true;
      });
      return [...livraisonMatieresToAdd, ...livraisonMatieresCollection];
    }
    return livraisonMatieresCollection;
  }

  protected convertDateFromClient(livraisonMatieres: ILivraisonMatieres): ILivraisonMatieres {
    return Object.assign({}, livraisonMatieres, {
      dateLivree: livraisonMatieres.dateLivree?.isValid() ? livraisonMatieres.dateLivree.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateLivree = res.body.dateLivree ? dayjs(res.body.dateLivree) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((livraisonMatieres: ILivraisonMatieres) => {
        livraisonMatieres.dateLivree = livraisonMatieres.dateLivree ? dayjs(livraisonMatieres.dateLivree) : undefined;
      });
    }
    return res;
  }
}
