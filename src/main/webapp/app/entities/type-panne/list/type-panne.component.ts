import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITypePanne } from '../type-panne.model';
import { TypePanneService } from '../service/type-panne.service';
import { TypePanneDeleteDialogComponent } from '../delete/type-panne-delete-dialog.component';

@Component({
  selector: 'jhi-type-panne',
  templateUrl: './type-panne.component.html',
})
export class TypePanneComponent implements OnInit {
  typePannes?: ITypePanne[];
  isLoading = false;

  constructor(protected typePanneService: TypePanneService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.typePanneService.query().subscribe({
      next: (res: HttpResponse<ITypePanne[]>) => {
        this.isLoading = false;
        this.typePannes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ITypePanne): number {
    return item.id!;
  }

  delete(typePanne: ITypePanne): void {
    const modalRef = this.modalService.open(TypePanneDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.typePanne = typePanne;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
