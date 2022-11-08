import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DemandeReparationsComponent } from './list/demande-reparations.component';
import { DemandeReparationsDetailComponent } from './detail/demande-reparations-detail.component';
import { DemandeReparationsUpdateComponent } from './update/demande-reparations-update.component';
import { DemandeReparationsDeleteDialogComponent } from './delete/demande-reparations-delete-dialog.component';
import { DemandeReparationsRoutingModule } from './route/demande-reparations-routing.module';
//import { SaveReparationsComponent } from './save-reparations/save-reparations.component';

@NgModule({
  imports: [SharedModule, DemandeReparationsRoutingModule],
  declarations: [
    DemandeReparationsComponent,
    DemandeReparationsDetailComponent,
    DemandeReparationsUpdateComponent,
    DemandeReparationsDeleteDialogComponent,
    //SaveReparationsComponent,
  ],
  entryComponents: [DemandeReparationsDeleteDialogComponent],
})
export class DemandeReparationsModule {}
