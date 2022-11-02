import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MarqueVehiculeComponent } from '../list/marque-vehicule.component';
import { MarqueVehiculeDetailComponent } from '../detail/marque-vehicule-detail.component';
import { MarqueVehiculeUpdateComponent } from '../update/marque-vehicule-update.component';
import { MarqueVehiculeRoutingResolveService } from './marque-vehicule-routing-resolve.service';

const marqueVehiculeRoute: Routes = [
  {
    path: '',
    component: MarqueVehiculeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MarqueVehiculeDetailComponent,
    resolve: {
      marqueVehicule: MarqueVehiculeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MarqueVehiculeUpdateComponent,
    resolve: {
      marqueVehicule: MarqueVehiculeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MarqueVehiculeUpdateComponent,
    resolve: {
      marqueVehicule: MarqueVehiculeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(marqueVehiculeRoute)],
  exports: [RouterModule],
})
export class MarqueVehiculeRoutingModule {}
