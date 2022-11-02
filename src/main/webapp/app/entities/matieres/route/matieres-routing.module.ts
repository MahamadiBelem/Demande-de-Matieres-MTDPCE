import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MatieresComponent } from '../list/matieres.component';
import { MatieresDetailComponent } from '../detail/matieres-detail.component';
import { MatieresUpdateComponent } from '../update/matieres-update.component';
import { MatieresRoutingResolveService } from './matieres-routing-resolve.service';

const matieresRoute: Routes = [
  {
    path: '',
    component: MatieresComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MatieresDetailComponent,
    resolve: {
      matieres: MatieresRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MatieresUpdateComponent,
    resolve: {
      matieres: MatieresRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MatieresUpdateComponent,
    resolve: {
      matieres: MatieresRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(matieresRoute)],
  exports: [RouterModule],
})
export class MatieresRoutingModule {}
