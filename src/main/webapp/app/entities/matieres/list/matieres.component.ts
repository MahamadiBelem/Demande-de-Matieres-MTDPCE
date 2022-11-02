import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMatieres } from '../matieres.model';
import { MatieresService } from '../service/matieres.service';
import { MatieresDeleteDialogComponent } from '../delete/matieres-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-matieres',
  templateUrl: './matieres.component.html',
})
export class MatieresComponent implements OnInit {
  matieres?: IMatieres[];
  isLoading = false;

  constructor(protected matieresService: MatieresService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.matieresService.query().subscribe({
      next: (res: HttpResponse<IMatieres[]>) => {
        this.isLoading = false;
        this.matieres = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IMatieres): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(matieres: IMatieres): void {
    const modalRef = this.modalService.open(MatieresDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.matieres = matieres;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
