import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDemandeMatieres } from '../demande-matieres.model';
import { DemandeMatieresService } from '../service/demande-matieres.service';
import { DemandeMatieresDeleteDialogComponent } from '../delete/demande-matieres-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { DemandeMatieresUpdateComponent } from '../update/demande-matieres-update.component';

@Component({
  selector: 'jhi-demande-matieres',
  templateUrl: './demande-matieres.component.html',
})
export class DemandeMatieresComponent implements OnInit {
  demandeMatieres?: IDemandeMatieres[];
  isLoading = false;

  closeResult = '';
  content = '';

  title = 'appBootstrap';

  constructor(protected demandeMatieresService: DemandeMatieresService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.demandeMatieresService.query().subscribe({
      next: (res: HttpResponse<IDemandeMatieres[]>) => {
        this.isLoading = false;
        this.demandeMatieres = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDemandeMatieres): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(demandeMatieres: IDemandeMatieres): void {
    const modalRef = this.modalService.open(DemandeMatieresDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.demandeMatieres = demandeMatieres;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  savemodale(): void {
    const savemodal = this.modalService.open(DemandeMatieresUpdateComponent, { size: 'lg', backdrop: 'static' });
  }

  /**

   * Write code on Method

   *

   * @return response()

   

  open(content:"content"): void {

    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {

      this.closeResult = `Closed with: ${result}`;

    }, (reason) => {

      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;

    });

  } 


  private getDismissReason(reason: any): string {

    if (reason === ModalDismissReasons.ESC) {

      return 'by pressing ESC';

    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {

      return 'by clicking on a backdrop';

    } else {

      return  `with: ${reason}`;

    }
    }
    */
  /**
  ouvrir(demandeMatieres: IDemandeMatieres): void {
    const modalRef = this.modalService.open(DemandeMatieresDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.demandeMatieres = demandeMatieres;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

 
 * 
  open(content) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  */
}
