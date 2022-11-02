import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RevisionVehiculeComponent } from '../list/revision-vehicule.component';
import { RevisionVehiculeDetailComponent } from '../detail/revision-vehicule-detail.component';
import { RevisionVehiculeUpdateComponent } from '../update/revision-vehicule-update.component';
import { RevisionVehiculeRoutingResolveService } from './revision-vehicule-routing-resolve.service';

const revisionVehiculeRoute: Routes = [
  {
    path: '',
    component: RevisionVehiculeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RevisionVehiculeDetailComponent,
    resolve: {
      revisionVehicule: RevisionVehiculeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RevisionVehiculeUpdateComponent,
    resolve: {
      revisionVehicule: RevisionVehiculeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RevisionVehiculeUpdateComponent,
    resolve: {
      revisionVehicule: RevisionVehiculeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(revisionVehiculeRoute)],
  exports: [RouterModule],
})
export class RevisionVehiculeRoutingModule {}
