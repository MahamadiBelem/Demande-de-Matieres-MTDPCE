import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDemandeMatieres, DemandeMatieres } from '../demande-matieres.model';
import { DemandeMatieresService } from '../service/demande-matieres.service';

@Injectable({ providedIn: 'root' })
export class DemandeMatieresRoutingResolveService implements Resolve<IDemandeMatieres> {
  constructor(protected service: DemandeMatieresService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDemandeMatieres> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((demandeMatieres: HttpResponse<DemandeMatieres>) => {
          if (demandeMatieres.body) {
            return of(demandeMatieres.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DemandeMatieres());
  }
}
