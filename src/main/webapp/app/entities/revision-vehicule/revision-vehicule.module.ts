import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RevisionVehiculeComponent } from './list/revision-vehicule.component';
import { RevisionVehiculeDetailComponent } from './detail/revision-vehicule-detail.component';
import { RevisionVehiculeUpdateComponent } from './update/revision-vehicule-update.component';
import { RevisionVehiculeDeleteDialogComponent } from './delete/revision-vehicule-delete-dialog.component';
import { RevisionVehiculeRoutingModule } from './route/revision-vehicule-routing.module';

@NgModule({
  imports: [SharedModule, RevisionVehiculeRoutingModule],
  declarations: [
    RevisionVehiculeComponent,
    RevisionVehiculeDetailComponent,
    RevisionVehiculeUpdateComponent,
    RevisionVehiculeDeleteDialogComponent,
  ],
  entryComponents: [RevisionVehiculeDeleteDialogComponent],
})
export class RevisionVehiculeModule {}
