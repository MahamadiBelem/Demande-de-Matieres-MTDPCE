import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStructure, getStructureIdentifier } from '../structure.model';

export type EntityResponseType = HttpResponse<IStructure>;
export type EntityArrayResponseType = HttpResponse<IStructure[]>;

@Injectable({ providedIn: 'root' })
export class StructureService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/structures');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(structure: IStructure): Observable<EntityResponseType> {
    return this.http.post<IStructure>(this.resourceUrl, structure, { observe: 'response' });
  }

  update(structure: IStructure): Observable<EntityResponseType> {
    return this.http.put<IStructure>(`${this.resourceUrl}/${getStructureIdentifier(structure) as number}`, structure, {
      observe: 'response',
    });
  }

  partialUpdate(structure: IStructure): Observable<EntityResponseType> {
    return this.http.patch<IStructure>(`${this.resourceUrl}/${getStructureIdentifier(structure) as number}`, structure, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStructure>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStructure[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStructureToCollectionIfMissing(
    structureCollection: IStructure[],
    ...structuresToCheck: (IStructure | null | undefined)[]
  ): IStructure[] {
    const structures: IStructure[] = structuresToCheck.filter(isPresent);
    if (structures.length > 0) {
      const structureCollectionIdentifiers = structureCollection.map(structureItem => getStructureIdentifier(structureItem)!);
      const structuresToAdd = structures.filter(structureItem => {
        const structureIdentifier = getStructureIdentifier(structureItem);
        if (structureIdentifier == null || structureCollectionIdentifiers.includes(structureIdentifier)) {
          return false;
        }
        structureCollectionIdentifiers.push(structureIdentifier);
        return true;
      });
      return [...structuresToAdd, ...structureCollection];
    }
    return structureCollection;
  }
}
