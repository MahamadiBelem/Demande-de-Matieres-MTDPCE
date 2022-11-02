import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRevisionVehicule } from '../revision-vehicule.model';
import { RevisionVehiculeService } from '../service/revision-vehicule.service';

@Component({
  templateUrl: './revision-vehicule-delete-dialog.component.html',
})
export class RevisionVehiculeDeleteDialogComponent {
  revisionVehicule?: IRevisionVehicule;

  constructor(protected revisionVehiculeService: RevisionVehiculeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.revisionVehiculeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
