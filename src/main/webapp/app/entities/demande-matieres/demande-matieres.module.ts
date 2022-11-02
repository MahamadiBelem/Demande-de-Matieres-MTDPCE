import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DemandeMatieresComponent } from './list/demande-matieres.component';
import { DemandeMatieresDetailComponent } from './detail/demande-matieres-detail.component';
import { DemandeMatieresUpdateComponent } from './update/demande-matieres-update.component';
import { DemandeMatieresDeleteDialogComponent } from './delete/demande-matieres-delete-dialog.component';
import { DemandeMatieresRoutingModule } from './route/demande-matieres-routing.module';
import { SavedemandeComponent } from './savedemande/savedemande.component';

@NgModule({
  imports: [SharedModule, DemandeMatieresRoutingModule],
  declarations: [
    DemandeMatieresComponent,
    DemandeMatieresDetailComponent,
    DemandeMatieresUpdateComponent,
    DemandeMatieresDeleteDialogComponent,
    SavedemandeComponent,
  ],
  entryComponents: [DemandeMatieresDeleteDialogComponent],
})
export class DemandeMatieresModule {}
