import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICarnetVehicule } from '../carnet-vehicule.model';
import { CarnetVehiculeService } from '../service/carnet-vehicule.service';
import { CarnetVehiculeDeleteDialogComponent } from '../delete/carnet-vehicule-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-carnet-vehicule',
  templateUrl: './carnet-vehicule.component.html',
})
export class CarnetVehiculeComponent implements OnInit {
  carnetVehicules?: ICarnetVehicule[];
  isLoading = false;

  constructor(protected carnetVehiculeService: CarnetVehiculeService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.carnetVehiculeService.query().subscribe({
      next: (res: HttpResponse<ICarnetVehicule[]>) => {
        this.isLoading = false;
        this.carnetVehicules = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ICarnetVehicule): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(carnetVehicule: ICarnetVehicule): void {
    const modalRef = this.modalService.open(CarnetVehiculeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.carnetVehicule = carnetVehicule;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
