import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMarqueVehicule, getMarqueVehiculeIdentifier } from '../marque-vehicule.model';

export type EntityResponseType = HttpResponse<IMarqueVehicule>;
export type EntityArrayResponseType = HttpResponse<IMarqueVehicule[]>;

@Injectable({ providedIn: 'root' })
export class MarqueVehiculeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/marque-vehicules');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(marqueVehicule: IMarqueVehicule): Observable<EntityResponseType> {
    return this.http.post<IMarqueVehicule>(this.resourceUrl, marqueVehicule, { observe: 'response' });
  }

  update(marqueVehicule: IMarqueVehicule): Observable<EntityResponseType> {
    return this.http.put<IMarqueVehicule>(`${this.resourceUrl}/${getMarqueVehiculeIdentifier(marqueVehicule) as number}`, marqueVehicule, {
      observe: 'response',
    });
  }

  partialUpdate(marqueVehicule: IMarqueVehicule): Observable<EntityResponseType> {
    return this.http.patch<IMarqueVehicule>(
      `${this.resourceUrl}/${getMarqueVehiculeIdentifier(marqueVehicule) as number}`,
      marqueVehicule,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMarqueVehicule>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMarqueVehicule[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMarqueVehiculeToCollectionIfMissing(
    marqueVehiculeCollection: IMarqueVehicule[],
    ...marqueVehiculesToCheck: (IMarqueVehicule | null | undefined)[]
  ): IMarqueVehicule[] {
    const marqueVehicules: IMarqueVehicule[] = marqueVehiculesToCheck.filter(isPresent);
    if (marqueVehicules.length > 0) {
      const marqueVehiculeCollectionIdentifiers = marqueVehiculeCollection.map(
        marqueVehiculeItem => getMarqueVehiculeIdentifier(marqueVehiculeItem)!
      );
      const marqueVehiculesToAdd = marqueVehicules.filter(marqueVehiculeItem => {
        const marqueVehiculeIdentifier = getMarqueVehiculeIdentifier(marqueVehiculeItem);
        if (marqueVehiculeIdentifier == null || marqueVehiculeCollectionIdentifiers.includes(marqueVehiculeIdentifier)) {
          return false;
        }
        marqueVehiculeCollectionIdentifiers.push(marqueVehiculeIdentifier);
        return true;
      });
      return [...marqueVehiculesToAdd, ...marqueVehiculeCollection];
    }
    return marqueVehiculeCollection;
  }
}
