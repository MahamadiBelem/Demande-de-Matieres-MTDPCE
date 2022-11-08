import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITypeMatiere } from '../type-matiere.model';
import { TypeMatiereService } from '../service/type-matiere.service';
import { TypeMatiereDeleteDialogComponent } from '../delete/type-matiere-delete-dialog.component';
import { TypeMatiereUpdateComponent } from '../update/type-matiere-update.component';

@Component({
  selector: 'jhi-type-matiere',
  templateUrl: './type-matiere.component.html',
})
export class TypeMatiereComponent implements OnInit {
  typeMatieres?: ITypeMatiere[];
  isLoading = false;

  constructor(protected typeMatiereService: TypeMatiereService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.typeMatiereService.query().subscribe({
      next: (res: HttpResponse<ITypeMatiere[]>) => {
        this.isLoading = false;
        this.typeMatieres = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  savemodale(): void {
    const savemodal = this.modalService.open(TypeMatiereUpdateComponent, { size: 'md', backdrop: 'static' });
  }

  trackId(_index: number, item: ITypeMatiere): number {
    return item.id!;
  }

  delete(typeMatiere: ITypeMatiere): void {
    const modalRef = this.modalService.open(TypeMatiereDeleteDialogComponent, { size: 'md', backdrop: 'static' });
    modalRef.componentInstance.typeMatiere = typeMatiere;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
