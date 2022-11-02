import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICarnetVehicule, CarnetVehicule } from '../carnet-vehicule.model';
import { CarnetVehiculeService } from '../service/carnet-vehicule.service';

@Injectable({ providedIn: 'root' })
export class CarnetVehiculeRoutingResolveService implements Resolve<ICarnetVehicule> {
  constructor(protected service: CarnetVehiculeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICarnetVehicule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((carnetVehicule: HttpResponse<CarnetVehicule>) => {
          if (carnetVehicule.body) {
            return of(carnetVehicule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CarnetVehicule());
  }
}
