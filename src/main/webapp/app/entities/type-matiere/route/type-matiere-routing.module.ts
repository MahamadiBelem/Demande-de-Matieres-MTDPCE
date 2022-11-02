import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TypeMatiereComponent } from '../list/type-matiere.component';
import { TypeMatiereDetailComponent } from '../detail/type-matiere-detail.component';
import { TypeMatiereUpdateComponent } from '../update/type-matiere-update.component';
import { TypeMatiereRoutingResolveService } from './type-matiere-routing-resolve.service';

const typeMatiereRoute: Routes = [
  {
    path: '',
    component: TypeMatiereComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeMatiereDetailComponent,
    resolve: {
      typeMatiere: TypeMatiereRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeMatiereUpdateComponent,
    resolve: {
      typeMatiere: TypeMatiereRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeMatiereUpdateComponent,
    resolve: {
      typeMatiere: TypeMatiereRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(typeMatiereRoute)],
  exports: [RouterModule],
})
export class TypeMatiereRoutingModule {}
