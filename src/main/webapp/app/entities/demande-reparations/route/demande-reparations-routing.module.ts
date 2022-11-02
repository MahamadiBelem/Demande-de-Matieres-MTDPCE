import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DemandeReparationsComponent } from '../list/demande-reparations.component';
import { DemandeReparationsDetailComponent } from '../detail/demande-reparations-detail.component';
import { DemandeReparationsUpdateComponent } from '../update/demande-reparations-update.component';
import { DemandeReparationsRoutingResolveService } from './demande-reparations-routing-resolve.service';

const demandeReparationsRoute: Routes = [
  {
    path: '',
    component: DemandeReparationsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DemandeReparationsDetailComponent,
    resolve: {
      demandeReparations: DemandeReparationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DemandeReparationsUpdateComponent,
    resolve: {
      demandeReparations: DemandeReparationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DemandeReparationsUpdateComponent,
    resolve: {
      demandeReparations: DemandeReparationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(demandeReparationsRoute)],
  exports: [RouterModule],
})
export class DemandeReparationsRoutingModule {}
