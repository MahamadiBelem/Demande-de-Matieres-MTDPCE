import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMatieres } from '../matieres.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-matieres-detail',
  templateUrl: './matieres-detail.component.html',
})
export class MatieresDetailComponent implements OnInit {
  matieres: IMatieres | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ matieres }) => {
      this.matieres = matieres;
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
