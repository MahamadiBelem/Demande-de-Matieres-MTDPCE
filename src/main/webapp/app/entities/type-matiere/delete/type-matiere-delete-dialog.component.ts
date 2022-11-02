import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITypeMatiere } from '../type-matiere.model';
import { TypeMatiereService } from '../service/type-matiere.service';

@Component({
  templateUrl: './type-matiere-delete-dialog.component.html',
})
export class TypeMatiereDeleteDialogComponent {
  typeMatiere?: ITypeMatiere;

  constructor(protected typeMatiereService: TypeMatiereService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeMatiereService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
