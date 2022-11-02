import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeMatiere, getTypeMatiereIdentifier } from '../type-matiere.model';

export type EntityResponseType = HttpResponse<ITypeMatiere>;
export type EntityArrayResponseType = HttpResponse<ITypeMatiere[]>;

@Injectable({ providedIn: 'root' })
export class TypeMatiereService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-matieres');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(typeMatiere: ITypeMatiere): Observable<EntityResponseType> {
    return this.http.post<ITypeMatiere>(this.resourceUrl, typeMatiere, { observe: 'response' });
  }

  update(typeMatiere: ITypeMatiere): Observable<EntityResponseType> {
    return this.http.put<ITypeMatiere>(`${this.resourceUrl}/${getTypeMatiereIdentifier(typeMatiere) as number}`, typeMatiere, {
      observe: 'response',
    });
  }

  partialUpdate(typeMatiere: ITypeMatiere): Observable<EntityResponseType> {
    return this.http.patch<ITypeMatiere>(`${this.resourceUrl}/${getTypeMatiereIdentifier(typeMatiere) as number}`, typeMatiere, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeMatiere>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeMatiere[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTypeMatiereToCollectionIfMissing(
    typeMatiereCollection: ITypeMatiere[],
    ...typeMatieresToCheck: (ITypeMatiere | null | undefined)[]
  ): ITypeMatiere[] {
    const typeMatieres: ITypeMatiere[] = typeMatieresToCheck.filter(isPresent);
    if (typeMatieres.length > 0) {
      const typeMatiereCollectionIdentifiers = typeMatiereCollection.map(typeMatiereItem => getTypeMatiereIdentifier(typeMatiereItem)!);
      const typeMatieresToAdd = typeMatieres.filter(typeMatiereItem => {
        const typeMatiereIdentifier = getTypeMatiereIdentifier(typeMatiereItem);
        if (typeMatiereIdentifier == null || typeMatiereCollectionIdentifiers.includes(typeMatiereIdentifier)) {
          return false;
        }
        typeMatiereCollectionIdentifiers.push(typeMatiereIdentifier);
        return true;
      });
      return [...typeMatieresToAdd, ...typeMatiereCollection];
    }
    return typeMatiereCollection;
  }
}
