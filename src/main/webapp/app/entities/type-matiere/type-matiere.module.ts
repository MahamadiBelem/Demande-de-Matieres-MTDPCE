import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TypeMatiereComponent } from './list/type-matiere.component';
import { TypeMatiereDetailComponent } from './detail/type-matiere-detail.component';
import { TypeMatiereUpdateComponent } from './update/type-matiere-update.component';
import { TypeMatiereDeleteDialogComponent } from './delete/type-matiere-delete-dialog.component';
import { TypeMatiereRoutingModule } from './route/type-matiere-routing.module';

@NgModule({
  imports: [SharedModule, TypeMatiereRoutingModule],
  declarations: [TypeMatiereComponent, TypeMatiereDetailComponent, TypeMatiereUpdateComponent, TypeMatiereDeleteDialogComponent],
  entryComponents: [TypeMatiereDeleteDialogComponent],
})
export class TypeMatiereModule {}
