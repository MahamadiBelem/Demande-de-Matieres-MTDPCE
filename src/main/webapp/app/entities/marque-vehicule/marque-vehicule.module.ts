import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MarqueVehiculeComponent } from './list/marque-vehicule.component';
import { MarqueVehiculeDetailComponent } from './detail/marque-vehicule-detail.component';
import { MarqueVehiculeUpdateComponent } from './update/marque-vehicule-update.component';
import { MarqueVehiculeDeleteDialogComponent } from './delete/marque-vehicule-delete-dialog.component';
import { MarqueVehiculeRoutingModule } from './route/marque-vehicule-routing.module';

@NgModule({
  imports: [SharedModule, MarqueVehiculeRoutingModule],
  declarations: [
    MarqueVehiculeComponent,
    MarqueVehiculeDetailComponent,
    MarqueVehiculeUpdateComponent,
    MarqueVehiculeDeleteDialogComponent,
  ],
  entryComponents: [MarqueVehiculeDeleteDialogComponent],
})
export class MarqueVehiculeModule {}
