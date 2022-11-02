import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMarqueVehicule, MarqueVehicule } from '../marque-vehicule.model';
import { MarqueVehiculeService } from '../service/marque-vehicule.service';

@Injectable({ providedIn: 'root' })
export class MarqueVehiculeRoutingResolveService implements Resolve<IMarqueVehicule> {
  constructor(protected service: MarqueVehiculeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMarqueVehicule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((marqueVehicule: HttpResponse<MarqueVehicule>) => {
          if (marqueVehicule.body) {
            return of(marqueVehicule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MarqueVehicule());
  }
}
