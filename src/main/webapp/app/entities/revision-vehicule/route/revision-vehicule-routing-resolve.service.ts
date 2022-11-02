import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRevisionVehicule, RevisionVehicule } from '../revision-vehicule.model';
import { RevisionVehiculeService } from '../service/revision-vehicule.service';

@Injectable({ providedIn: 'root' })
export class RevisionVehiculeRoutingResolveService implements Resolve<IRevisionVehicule> {
  constructor(protected service: RevisionVehiculeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRevisionVehicule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((revisionVehicule: HttpResponse<RevisionVehicule>) => {
          if (revisionVehicule.body) {
            return of(revisionVehicule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RevisionVehicule());
  }
}
