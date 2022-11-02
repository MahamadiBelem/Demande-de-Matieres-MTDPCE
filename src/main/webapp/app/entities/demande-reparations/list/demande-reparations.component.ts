import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDemandeReparations } from '../demande-reparations.model';
import { DemandeReparationsService } from '../service/demande-reparations.service';
import { DemandeReparationsDeleteDialogComponent } from '../delete/demande-reparations-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-demande-reparations',
  templateUrl: './demande-reparations.component.html',
})
export class DemandeReparationsComponent implements OnInit {
  demandeReparations?: IDemandeReparations[];
  isLoading = false;

  constructor(
    protected demandeReparationsService: DemandeReparationsService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.demandeReparationsService.query().subscribe({
      next: (res: HttpResponse<IDemandeReparations[]>) => {
        this.isLoading = false;
        this.demandeReparations = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDemandeReparations): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(demandeReparations: IDemandeReparations): void {
    const modalRef = this.modalService.open(DemandeReparationsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.demandeReparations = demandeReparations;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
