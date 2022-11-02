import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LivraisonMatieresComponent } from '../list/livraison-matieres.component';
import { LivraisonMatieresDetailComponent } from '../detail/livraison-matieres-detail.component';
import { LivraisonMatieresUpdateComponent } from '../update/livraison-matieres-update.component';
import { LivraisonMatieresRoutingResolveService } from './livraison-matieres-routing-resolve.service';

const livraisonMatieresRoute: Routes = [
  {
    path: '',
    component: LivraisonMatieresComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LivraisonMatieresDetailComponent,
    resolve: {
      livraisonMatieres: LivraisonMatieresRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LivraisonMatieresUpdateComponent,
    resolve: {
      livraisonMatieres: LivraisonMatieresRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LivraisonMatieresUpdateComponent,
    resolve: {
      livraisonMatieres: LivraisonMatieresRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(livraisonMatieresRoute)],
  exports: [RouterModule],
})
export class LivraisonMatieresRoutingModule {}
