import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeMatiere, TypeMatiere } from '../type-matiere.model';
import { TypeMatiereService } from '../service/type-matiere.service';

@Injectable({ providedIn: 'root' })
export class TypeMatiereRoutingResolveService implements Resolve<ITypeMatiere> {
  constructor(protected service: TypeMatiereService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITypeMatiere> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((typeMatiere: HttpResponse<TypeMatiere>) => {
          if (typeMatiere.body) {
            return of(typeMatiere.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TypeMatiere());
  }
}
