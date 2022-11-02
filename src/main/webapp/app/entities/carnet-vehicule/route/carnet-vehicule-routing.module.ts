import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CarnetVehiculeComponent } from '../list/carnet-vehicule.component';
import { CarnetVehiculeDetailComponent } from '../detail/carnet-vehicule-detail.component';
import { CarnetVehiculeUpdateComponent } from '../update/carnet-vehicule-update.component';
import { CarnetVehiculeRoutingResolveService } from './carnet-vehicule-routing-resolve.service';

const carnetVehiculeRoute: Routes = [
  {
    path: '',
    component: CarnetVehiculeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CarnetVehiculeDetailComponent,
    resolve: {
      carnetVehicule: CarnetVehiculeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CarnetVehiculeUpdateComponent,
    resolve: {
      carnetVehicule: CarnetVehiculeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CarnetVehiculeUpdateComponent,
    resolve: {
      carnetVehicule: CarnetVehiculeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(carnetVehiculeRoute)],
  exports: [RouterModule],
})
export class CarnetVehiculeRoutingModule {}
