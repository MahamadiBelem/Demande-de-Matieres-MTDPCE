import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypePanne, getTypePanneIdentifier } from '../type-panne.model';

export type EntityResponseType = HttpResponse<ITypePanne>;
export type EntityArrayResponseType = HttpResponse<ITypePanne[]>;

@Injectable({ providedIn: 'root' })
export class TypePanneService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-pannes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(typePanne: ITypePanne): Observable<EntityResponseType> {
    return this.http.post<ITypePanne>(this.resourceUrl, typePanne, { observe: 'response' });
  }

  update(typePanne: ITypePanne): Observable<EntityResponseType> {
    return this.http.put<ITypePanne>(`${this.resourceUrl}/${getTypePanneIdentifier(typePanne) as number}`, typePanne, {
      observe: 'response',
    });
  }

  partialUpdate(typePanne: ITypePanne): Observable<EntityResponseType> {
    return this.http.patch<ITypePanne>(`${this.resourceUrl}/${getTypePanneIdentifier(typePanne) as number}`, typePanne, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypePanne>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypePanne[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTypePanneToCollectionIfMissing(
    typePanneCollection: ITypePanne[],
    ...typePannesToCheck: (ITypePanne | null | undefined)[]
  ): ITypePanne[] {
    const typePannes: ITypePanne[] = typePannesToCheck.filter(isPresent);
    if (typePannes.length > 0) {
      const typePanneCollectionIdentifiers = typePanneCollection.map(typePanneItem => getTypePanneIdentifier(typePanneItem)!);
      const typePannesToAdd = typePannes.filter(typePanneItem => {
        const typePanneIdentifier = getTypePanneIdentifier(typePanneItem);
        if (typePanneIdentifier == null || typePanneCollectionIdentifiers.includes(typePanneIdentifier)) {
          return false;
        }
        typePanneCollectionIdentifiers.push(typePanneIdentifier);
        return true;
      });
      return [...typePannesToAdd, ...typePanneCollection];
    }
    return typePanneCollection;
  }
}
