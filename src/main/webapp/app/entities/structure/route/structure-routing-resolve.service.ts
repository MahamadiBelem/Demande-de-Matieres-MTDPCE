import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStructure, Structure } from '../structure.model';
import { StructureService } from '../service/structure.service';

@Injectable({ providedIn: 'root' })
export class StructureRoutingResolveService implements Resolve<IStructure> {
  constructor(protected service: StructureService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStructure> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((structure: HttpResponse<Structure>) => {
          if (structure.body) {
            return of(structure.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Structure());
  }
}
