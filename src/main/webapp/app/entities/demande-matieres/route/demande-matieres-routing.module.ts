import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DemandeMatieresComponent } from '../list/demande-matieres.component';
import { DemandeMatieresDetailComponent } from '../detail/demande-matieres-detail.component';
import { DemandeMatieresUpdateComponent } from '../update/demande-matieres-update.component';
import { DemandeMatieresRoutingResolveService } from './demande-matieres-routing-resolve.service';

const demandeMatieresRoute: Routes = [
  {
    path: '',
    component: DemandeMatieresComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DemandeMatieresDetailComponent,
    resolve: {
      demandeMatieres: DemandeMatieresRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DemandeMatieresUpdateComponent,
    resolve: {
      demandeMatieres: DemandeMatieresRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DemandeMatieresUpdateComponent,
    resolve: {
      demandeMatieres: DemandeMatieresRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(demandeMatieresRoute)],
  exports: [RouterModule],
})
export class DemandeMatieresRoutingModule {}
