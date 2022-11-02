import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMatieres, Matieres } from '../matieres.model';
import { MatieresService } from '../service/matieres.service';

@Injectable({ providedIn: 'root' })
export class MatieresRoutingResolveService implements Resolve<IMatieres> {
  constructor(protected service: MatieresService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMatieres> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((matieres: HttpResponse<Matieres>) => {
          if (matieres.body) {
            return of(matieres.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Matieres());
  }
}
