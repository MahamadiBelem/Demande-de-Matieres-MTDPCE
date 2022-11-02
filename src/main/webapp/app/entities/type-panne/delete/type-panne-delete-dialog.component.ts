import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITypePanne } from '../type-panne.model';
import { TypePanneService } from '../service/type-panne.service';

@Component({
  templateUrl: './type-panne-delete-dialog.component.html',
})
export class TypePanneDeleteDialogComponent {
  typePanne?: ITypePanne;

  constructor(protected typePanneService: TypePanneService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typePanneService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
