import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMarqueVehicule, MarqueVehicule } from '../marque-vehicule.model';
import { MarqueVehiculeService } from '../service/marque-vehicule.service';

@Component({
  selector: 'jhi-marque-vehicule-update',
  templateUrl: './marque-vehicule-update.component.html',
})
export class MarqueVehiculeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    libelleMarqueVehicule: [null, [Validators.required, Validators.minLength(3)]],
  });

  constructor(
    protected marqueVehiculeService: MarqueVehiculeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ marqueVehicule }) => {
      this.updateForm(marqueVehicule);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const marqueVehicule = this.createFromForm();
    if (marqueVehicule.id !== undefined) {
      this.subscribeToSaveResponse(this.marqueVehiculeService.update(marqueVehicule));
    } else {
      this.subscribeToSaveResponse(this.marqueVehiculeService.create(marqueVehicule));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMarqueVehicule>>): void {
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

  protected updateForm(marqueVehicule: IMarqueVehicule): void {
    this.editForm.patchValue({
      id: marqueVehicule.id,
      libelleMarqueVehicule: marqueVehicule.libelleMarqueVehicule,
    });
  }

  protected createFromForm(): IMarqueVehicule {
    return {
      ...new MarqueVehicule(),
      id: this.editForm.get(['id'])!.value,
      libelleMarqueVehicule: this.editForm.get(['libelleMarqueVehicule'])!.value,
    };
  }
}
