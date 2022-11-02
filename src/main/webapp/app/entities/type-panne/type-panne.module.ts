import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TypePanneComponent } from './list/type-panne.component';
import { TypePanneDetailComponent } from './detail/type-panne-detail.component';
import { TypePanneUpdateComponent } from './update/type-panne-update.component';
import { TypePanneDeleteDialogComponent } from './delete/type-panne-delete-dialog.component';
import { TypePanneRoutingModule } from './route/type-panne-routing.module';

@NgModule({
  imports: [SharedModule, TypePanneRoutingModule],
  declarations: [TypePanneComponent, TypePanneDetailComponent, TypePanneUpdateComponent, TypePanneDeleteDialogComponent],
  entryComponents: [TypePanneDeleteDialogComponent],
})
export class TypePanneModule {}
