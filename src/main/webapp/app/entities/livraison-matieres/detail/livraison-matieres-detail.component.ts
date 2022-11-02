import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILivraisonMatieres } from '../livraison-matieres.model';

@Component({
  selector: 'jhi-livraison-matieres-detail',
  templateUrl: './livraison-matieres-detail.component.html',
})
export class LivraisonMatieresDetailComponent implements OnInit {
  livraisonMatieres: ILivraisonMatieres | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ livraisonMatieres }) => {
      this.livraisonMatieres = livraisonMatieres;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
