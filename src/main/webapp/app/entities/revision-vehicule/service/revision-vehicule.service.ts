import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRevisionVehicule, getRevisionVehiculeIdentifier } from '../revision-vehicule.model';

export type EntityResponseType = HttpResponse<IRevisionVehicule>;
export type EntityArrayResponseType = HttpResponse<IRevisionVehicule[]>;

@Injectable({ providedIn: 'root' })
export class RevisionVehiculeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/revision-vehicules');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(revisionVehicule: IRevisionVehicule): Observable<EntityResponseType> {
    return this.http.post<IRevisionVehicule>(this.resourceUrl, revisionVehicule, { observe: 'response' });
  }

  update(revisionVehicule: IRevisionVehicule): Observable<EntityResponseType> {
    return this.http.put<IRevisionVehicule>(
      `${this.resourceUrl}/${getRevisionVehiculeIdentifier(revisionVehicule) as number}`,
      revisionVehicule,
      { observe: 'response' }
    );
  }

  partialUpdate(revisionVehicule: IRevisionVehicule): Observable<EntityResponseType> {
    return this.http.patch<IRevisionVehicule>(
      `${this.resourceUrl}/${getRevisionVehiculeIdentifier(revisionVehicule) as number}`,
      revisionVehicule,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRevisionVehicule>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRevisionVehicule[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRevisionVehiculeToCollectionIfMissing(
    revisionVehiculeCollection: IRevisionVehicule[],
    ...revisionVehiculesToCheck: (IRevisionVehicule | null | undefined)[]
  ): IRevisionVehicule[] {
    const revisionVehicules: IRevisionVehicule[] = revisionVehiculesToCheck.filter(isPresent);
    if (revisionVehicules.length > 0) {
      const revisionVehiculeCollectionIdentifiers = revisionVehiculeCollection.map(
        revisionVehiculeItem => getRevisionVehiculeIdentifier(revisionVehiculeItem)!
      );
      const revisionVehiculesToAdd = revisionVehicules.filter(revisionVehiculeItem => {
        const revisionVehiculeIdentifier = getRevisionVehiculeIdentifier(revisionVehiculeItem);
        if (revisionVehiculeIdentifier == null || revisionVehiculeCollectionIdentifiers.includes(revisionVehiculeIdentifier)) {
          return false;
        }
        revisionVehiculeCollectionIdentifiers.push(revisionVehiculeIdentifier);
        return true;
      });
      return [...revisionVehiculesToAdd, ...revisionVehiculeCollection];
    }
    return revisionVehiculeCollection;
  }
}
