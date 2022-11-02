import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDemandeMatieres } from '../demande-matieres.model';
import { DemandeMatieresService } from '../service/demande-matieres.service';

@Component({
  templateUrl: './demande-matieres-delete-dialog.component.html',
})
export class DemandeMatieresDeleteDialogComponent {
  demandeMatieres?: IDemandeMatieres;

  constructor(protected demandeMatieresService: DemandeMatieresService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.demandeMatieresService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
