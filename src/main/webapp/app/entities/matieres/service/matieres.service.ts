import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMatieres, getMatieresIdentifier } from '../matieres.model';

export type EntityResponseType = HttpResponse<IMatieres>;
export type EntityArrayResponseType = HttpResponse<IMatieres[]>;

@Injectable({ providedIn: 'root' })
export class MatieresService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/matieres');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(matieres: IMatieres): Observable<EntityResponseType> {
    return this.http.post<IMatieres>(this.resourceUrl, matieres, { observe: 'response' });
  }

  update(matieres: IMatieres): Observable<EntityResponseType> {
    return this.http.put<IMatieres>(`${this.resourceUrl}/${getMatieresIdentifier(matieres) as number}`, matieres, { observe: 'response' });
  }

  partialUpdate(matieres: IMatieres): Observable<EntityResponseType> {
    return this.http.patch<IMatieres>(`${this.resourceUrl}/${getMatieresIdentifier(matieres) as number}`, matieres, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMatieres>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMatieres[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMatieresToCollectionIfMissing(matieresCollection: IMatieres[], ...matieresToCheck: (IMatieres | null | undefined)[]): IMatieres[] {
    const matieres: IMatieres[] = matieresToCheck.filter(isPresent);
    if (matieres.length > 0) {
      const matieresCollectionIdentifiers = matieresCollection.map(matieresItem => getMatieresIdentifier(matieresItem)!);
      const matieresToAdd = matieres.filter(matieresItem => {
        const matieresIdentifier = getMatieresIdentifier(matieresItem);
        if (matieresIdentifier == null || matieresCollectionIdentifiers.includes(matieresIdentifier)) {
          return false;
        }
        matieresCollectionIdentifiers.push(matieresIdentifier);
        return true;
      });
      return [...matieresToAdd, ...matieresCollection];
    }
    return matieresCollection;
  }
}
