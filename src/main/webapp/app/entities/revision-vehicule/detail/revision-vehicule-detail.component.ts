import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRevisionVehicule } from '../revision-vehicule.model';

@Component({
  selector: 'jhi-revision-vehicule-detail',
  templateUrl: './revision-vehicule-detail.component.html',
})
export class RevisionVehiculeDetailComponent implements OnInit {
  revisionVehicule: IRevisionVehicule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ revisionVehicule }) => {
      this.revisionVehicule = revisionVehicule;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
