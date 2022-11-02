import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILivraisonMatieres } from '../livraison-matieres.model';
import { LivraisonMatieresService } from '../service/livraison-matieres.service';

@Component({
  templateUrl: './livraison-matieres-delete-dialog.component.html',
})
export class LivraisonMatieresDeleteDialogComponent {
  livraisonMatieres?: ILivraisonMatieres;

  constructor(protected livraisonMatieresService: LivraisonMatieresService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.livraisonMatieresService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
