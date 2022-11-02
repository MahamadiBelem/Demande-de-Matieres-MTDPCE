import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TypePanneComponent } from '../list/type-panne.component';
import { TypePanneDetailComponent } from '../detail/type-panne-detail.component';
import { TypePanneUpdateComponent } from '../update/type-panne-update.component';
import { TypePanneRoutingResolveService } from './type-panne-routing-resolve.service';

const typePanneRoute: Routes = [
  {
    path: '',
    component: TypePanneComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypePanneDetailComponent,
    resolve: {
      typePanne: TypePanneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypePanneUpdateComponent,
    resolve: {
      typePanne: TypePanneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypePanneUpdateComponent,
    resolve: {
      typePanne: TypePanneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(typePanneRoute)],
  exports: [RouterModule],
})
export class TypePanneRoutingModule {}
