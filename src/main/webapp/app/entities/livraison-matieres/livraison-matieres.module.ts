import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LivraisonMatieresComponent } from './list/livraison-matieres.component';
import { LivraisonMatieresDetailComponent } from './detail/livraison-matieres-detail.component';
import { LivraisonMatieresUpdateComponent } from './update/livraison-matieres-update.component';
import { LivraisonMatieresDeleteDialogComponent } from './delete/livraison-matieres-delete-dialog.component';
import { LivraisonMatieresRoutingModule } from './route/livraison-matieres-routing.module';

@NgModule({
  imports: [SharedModule, LivraisonMatieresRoutingModule],
  declarations: [
    LivraisonMatieresComponent,
    LivraisonMatieresDetailComponent,
    LivraisonMatieresUpdateComponent,
    LivraisonMatieresDeleteDialogComponent,
  ],
  entryComponents: [LivraisonMatieresDeleteDialogComponent],
})
export class LivraisonMatieresModule {}
