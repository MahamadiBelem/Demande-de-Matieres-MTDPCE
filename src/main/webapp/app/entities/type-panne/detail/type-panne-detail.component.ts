import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypePanne } from '../type-panne.model';

@Component({
  selector: 'jhi-type-panne-detail',
  templateUrl: './type-panne-detail.component.html',
})
export class TypePanneDetailComponent implements OnInit {
  typePanne: ITypePanne | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typePanne }) => {
      this.typePanne = typePanne;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
