import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypeMatiere } from '../type-matiere.model';

@Component({
  selector: 'jhi-type-matiere-detail',
  templateUrl: './type-matiere-detail.component.html',
})
export class TypeMatiereDetailComponent implements OnInit {
  typeMatiere: ITypeMatiere | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeMatiere }) => {
      this.typeMatiere = typeMatiere;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
