import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDemandeReparations } from '../demande-reparations.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-demande-reparations-detail',
  templateUrl: './demande-reparations-detail.component.html',
})
export class DemandeReparationsDetailComponent implements OnInit {
  demandeReparations: IDemandeReparations | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demandeReparations }) => {
      this.demandeReparations = demandeReparations;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
