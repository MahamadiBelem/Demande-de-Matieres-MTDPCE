import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDemandeReparations, DemandeReparations } from '../demande-reparations.model';
import { DemandeReparationsService } from '../service/demande-reparations.service';

@Injectable({ providedIn: 'root' })
export class DemandeReparationsRoutingResolveService implements Resolve<IDemandeReparations> {
  constructor(protected service: DemandeReparationsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDemandeReparations> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((demandeReparations: HttpResponse<DemandeReparations>) => {
          if (demandeReparations.body) {
            return of(demandeReparations.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DemandeReparations());
  }
}
