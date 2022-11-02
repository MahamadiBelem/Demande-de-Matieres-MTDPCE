import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRevisionVehicule } from '../revision-vehicule.model';
import { RevisionVehiculeService } from '../service/revision-vehicule.service';
import { RevisionVehiculeDeleteDialogComponent } from '../delete/revision-vehicule-delete-dialog.component';

@Component({
  selector: 'jhi-revision-vehicule',
  templateUrl: './revision-vehicule.component.html',
})
export class RevisionVehiculeComponent implements OnInit {
  revisionVehicules?: IRevisionVehicule[];
  isLoading = false;

  constructor(protected revisionVehiculeService: RevisionVehiculeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.revisionVehiculeService.query().subscribe({
      next: (res: HttpResponse<IRevisionVehicule[]>) => {
        this.isLoading = false;
        this.revisionVehicules = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IRevisionVehicule): number {
    return item.id!;
  }

  delete(revisionVehicule: IRevisionVehicule): void {
    const modalRef = this.modalService.open(RevisionVehiculeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.revisionVehicule = revisionVehicule;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
