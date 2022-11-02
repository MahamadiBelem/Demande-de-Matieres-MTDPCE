import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMarqueVehicule } from '../marque-vehicule.model';
import { MarqueVehiculeService } from '../service/marque-vehicule.service';

@Component({
  templateUrl: './marque-vehicule-delete-dialog.component.html',
})
export class MarqueVehiculeDeleteDialogComponent {
  marqueVehicule?: IMarqueVehicule;

  constructor(protected marqueVehiculeService: MarqueVehiculeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.marqueVehiculeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
