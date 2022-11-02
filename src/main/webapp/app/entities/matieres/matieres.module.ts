import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MatieresComponent } from './list/matieres.component';
import { MatieresDetailComponent } from './detail/matieres-detail.component';
import { MatieresUpdateComponent } from './update/matieres-update.component';
import { MatieresDeleteDialogComponent } from './delete/matieres-delete-dialog.component';
import { MatieresRoutingModule } from './route/matieres-routing.module';

@NgModule({
  imports: [SharedModule, MatieresRoutingModule],
  declarations: [MatieresComponent, MatieresDetailComponent, MatieresUpdateComponent, MatieresDeleteDialogComponent],
  entryComponents: [MatieresDeleteDialogComponent],
})
export class MatieresModule {}
