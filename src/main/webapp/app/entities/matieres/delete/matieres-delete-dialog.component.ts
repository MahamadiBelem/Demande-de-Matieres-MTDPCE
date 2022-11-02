import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMatieres } from '../matieres.model';
import { MatieresService } from '../service/matieres.service';

@Component({
  templateUrl: './matieres-delete-dialog.component.html',
})
export class MatieresDeleteDialogComponent {
  matieres?: IMatieres;

  constructor(protected matieresService: MatieresService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.matieresService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
