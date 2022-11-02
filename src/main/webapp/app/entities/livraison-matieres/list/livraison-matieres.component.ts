import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILivraisonMatieres } from '../livraison-matieres.model';
import { LivraisonMatieresService } from '../service/livraison-matieres.service';
import { LivraisonMatieresDeleteDialogComponent } from '../delete/livraison-matieres-delete-dialog.component';

@Component({
  selector: 'jhi-livraison-matieres',
  templateUrl: './livraison-matieres.component.html',
})
export class LivraisonMatieresComponent implements OnInit {
  livraisonMatieres?: ILivraisonMatieres[];
  isLoading = false;

  constructor(protected livraisonMatieresService: LivraisonMatieresService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.livraisonMatieresService.query().subscribe({
      next: (res: HttpResponse<ILivraisonMatieres[]>) => {
        this.isLoading = false;
        this.livraisonMatieres = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ILivraisonMatieres): number {
    return item.id!;
  }

  delete(livraisonMatieres: ILivraisonMatieres): void {
    const modalRef = this.modalService.open(LivraisonMatieresDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.livraisonMatieres = livraisonMatieres;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
