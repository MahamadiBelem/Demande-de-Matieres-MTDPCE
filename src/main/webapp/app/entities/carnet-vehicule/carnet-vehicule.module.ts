import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CarnetVehiculeComponent } from './list/carnet-vehicule.component';
import { CarnetVehiculeDetailComponent } from './detail/carnet-vehicule-detail.component';
import { CarnetVehiculeUpdateComponent } from './update/carnet-vehicule-update.component';
import { CarnetVehiculeDeleteDialogComponent } from './delete/carnet-vehicule-delete-dialog.component';
import { CarnetVehiculeRoutingModule } from './route/carnet-vehicule-routing.module';

@NgModule({
  imports: [SharedModule, CarnetVehiculeRoutingModule],
  declarations: [
    CarnetVehiculeComponent,
    CarnetVehiculeDetailComponent,
    CarnetVehiculeUpdateComponent,
    CarnetVehiculeDeleteDialogComponent,
  ],
  entryComponents: [CarnetVehiculeDeleteDialogComponent],
})
export class CarnetVehiculeModule {}
