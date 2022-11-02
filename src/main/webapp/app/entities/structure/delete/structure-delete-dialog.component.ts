import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStructure } from '../structure.model';
import { StructureService } from '../service/structure.service';

@Component({
  templateUrl: './structure-delete-dialog.component.html',
})
export class StructureDeleteDialogComponent {
  structure?: IStructure;

  constructor(protected structureService: StructureService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.structureService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
