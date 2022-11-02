import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILivraisonMatieres, LivraisonMatieres } from '../livraison-matieres.model';
import { LivraisonMatieresService } from '../service/livraison-matieres.service';

@Injectable({ providedIn: 'root' })
export class LivraisonMatieresRoutingResolveService implements Resolve<ILivraisonMatieres> {
  constructor(protected service: LivraisonMatieresService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILivraisonMatieres> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((livraisonMatieres: HttpResponse<LivraisonMatieres>) => {
          if (livraisonMatieres.body) {
            return of(livraisonMatieres.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LivraisonMatieres());
  }
}
