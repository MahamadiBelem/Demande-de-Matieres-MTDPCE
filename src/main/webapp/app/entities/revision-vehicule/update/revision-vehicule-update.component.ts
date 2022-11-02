import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRevisionVehicule, RevisionVehicule } from '../revision-vehicule.model';
import { RevisionVehiculeService } from '../service/revision-vehicule.service';
import { ICarnetVehicule } from 'app/entities/carnet-vehicule/carnet-vehicule.model';
import { CarnetVehiculeService } from 'app/entities/carnet-vehicule/service/carnet-vehicule.service';

@Component({
  selector: 'jhi-revision-vehicule-update',
  templateUrl: './revision-vehicule-update.component.html',
})
export class RevisionVehiculeUpdateComponent implements OnInit {
  isSaving = false;

  carnetVehiculesSharedCollection: ICarnetVehicule[] = [];

  editForm = this.fb.group({
    id: [],
    libelleRevisionVehicule: [null, [Validators.required, Validators.minLength(3)]],
    carnetVehicule: [],
  });

  constructor(
    protected revisionVehiculeService: RevisionVehiculeService,
    protected carnetVehiculeService: CarnetVehiculeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ revisionVehicule }) => {
      this.updateForm(revisionVehicule);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const revisionVehicule = this.createFromForm();
    if (revisionVehicule.id !== undefined) {
      this.subscribeToSaveResponse(this.revisionVehiculeService.update(revisionVehicule));
    } else {
      this.subscribeToSaveResponse(this.revisionVehiculeService.create(revisionVehicule));
    }
  }

  trackCarnetVehiculeById(_index: number, item: ICarnetVehicule): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRevisionVehicule>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(revisionVehicule: IRevisionVehicule): void {
    this.editForm.patchValue({
      id: revisionVehicule.id,
      libelleRevisionVehicule: revisionVehicule.libelleRevisionVehicule,
      carnetVehicule: revisionVehicule.carnetVehicule,
    });

    this.carnetVehiculesSharedCollection = this.carnetVehiculeService.addCarnetVehiculeToCollectionIfMissing(
      this.carnetVehiculesSharedCollection,
      revisionVehicule.carnetVehicule
    );
  }

  protected loadRelationshipsOptions(): void {
    this.carnetVehiculeService
      .query()
      .pipe(map((res: HttpResponse<ICarnetVehicule[]>) => res.body ?? []))
      .pipe(
        map((carnetVehicules: ICarnetVehicule[]) =>
          this.carnetVehiculeService.addCarnetVehiculeToCollectionIfMissing(carnetVehicules, this.editForm.get('carnetVehicule')!.value)
        )
      )
      .subscribe((carnetVehicules: ICarnetVehicule[]) => (this.carnetVehiculesSharedCollection = carnetVehicules));
  }

  protected createFromForm(): IRevisionVehicule {
    return {
      ...new RevisionVehicule(),
      id: this.editForm.get(['id'])!.value,
      libelleRevisionVehicule: this.editForm.get(['libelleRevisionVehicule'])!.value,
      carnetVehicule: this.editForm.get(['carnetVehicule'])!.value,
    };
  }
}
