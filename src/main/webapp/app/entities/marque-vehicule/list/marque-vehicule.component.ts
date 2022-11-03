import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMarqueVehicule } from '../marque-vehicule.model';
import { MarqueVehiculeService } from '../service/marque-vehicule.service';
import { MarqueVehiculeDeleteDialogComponent } from '../delete/marque-vehicule-delete-dialog.component';
import { MarqueVehiculeUpdateComponent } from '../update/marque-vehicule-update.component';

@Component({
  selector: 'jhi-marque-vehicule',
  templateUrl: './marque-vehicule.component.html',
})
export class MarqueVehiculeComponent implements OnInit {
  marqueVehicules?: IMarqueVehicule[];
  isLoading = false;

  constructor(protected marqueVehiculeService: MarqueVehiculeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.marqueVehiculeService.query().subscribe({
      next: (res: HttpResponse<IMarqueVehicule[]>) => {
        this.isLoading = false;
        this.marqueVehicules = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  //
  savemodale(): void {
    const savemodal = this.modalService.open(MarqueVehiculeUpdateComponent, { size: 'lg', backdrop: 'static' });
  }

  trackId(_index: number, item: IMarqueVehicule): number {
    return item.id!;
  }

  delete(marqueVehicule: IMarqueVehicule): void {
    const modalRef = this.modalService.open(MarqueVehiculeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.marqueVehicule = marqueVehicule;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
