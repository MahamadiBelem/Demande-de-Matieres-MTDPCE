import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICarnetVehicule } from '../carnet-vehicule.model';
import { CarnetVehiculeService } from '../service/carnet-vehicule.service';

@Component({
  templateUrl: './carnet-vehicule-delete-dialog.component.html',
})
export class CarnetVehiculeDeleteDialogComponent {
  carnetVehicule?: ICarnetVehicule;

  constructor(protected carnetVehiculeService: CarnetVehiculeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.carnetVehiculeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
