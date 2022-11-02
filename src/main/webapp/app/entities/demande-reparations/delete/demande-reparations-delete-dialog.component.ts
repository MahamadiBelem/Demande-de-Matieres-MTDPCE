import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDemandeReparations } from '../demande-reparations.model';
import { DemandeReparationsService } from '../service/demande-reparations.service';

@Component({
  templateUrl: './demande-reparations-delete-dialog.component.html',
})
export class DemandeReparationsDeleteDialogComponent {
  demandeReparations?: IDemandeReparations;

  constructor(protected demandeReparationsService: DemandeReparationsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.demandeReparationsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
