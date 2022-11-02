import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStructure } from '../structure.model';
import { StructureService } from '../service/structure.service';
import { StructureDeleteDialogComponent } from '../delete/structure-delete-dialog.component';
import { StructureUpdateComponent } from '../update/structure-update.component';

@Component({
  selector: 'jhi-structure',
  templateUrl: './structure.component.html',
})
export class StructureComponent implements OnInit {
  structures?: IStructure[];
  isLoading = false;

  constructor(protected structureService: StructureService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.structureService.query().subscribe({
      next: (res: HttpResponse<IStructure[]>) => {
        this.isLoading = false;
        this.structures = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IStructure): number {
    return item.id!;
  }

  delete(structure: IStructure): void {
    const modalRef = this.modalService.open(StructureDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.structure = structure;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
  //
  savemodale(): void {
    const savemodal = this.modalService.open(StructureUpdateComponent, { size: 'lg', backdrop: 'static' });
  }
}
