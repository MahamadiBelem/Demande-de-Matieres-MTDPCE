import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITypePanne, TypePanne } from '../type-panne.model';
import { TypePanneService } from '../service/type-panne.service';
import { IDemandeReparations } from 'app/entities/demande-reparations/demande-reparations.model';
import { DemandeReparationsService } from 'app/entities/demande-reparations/service/demande-reparations.service';

@Component({
  selector: 'jhi-type-panne-update',
  templateUrl: './type-panne-update.component.html',
})
export class TypePanneUpdateComponent implements OnInit {
  isSaving = false;

  demandeReparationsSharedCollection: IDemandeReparations[] = [];

  editForm = this.fb.group({
    id: [],
    libelleTypePanne: [null, [Validators.required, Validators.minLength(3)]],
    statutSup: [],
    demandeReparations: [],
  });

  constructor(
    protected typePanneService: TypePanneService,
    protected demandeReparationsService: DemandeReparationsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typePanne }) => {
      this.updateForm(typePanne);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typePanne = this.createFromForm();
    if (typePanne.id !== undefined) {
      this.subscribeToSaveResponse(this.typePanneService.update(typePanne));
    } else {
      this.subscribeToSaveResponse(this.typePanneService.create(typePanne));
    }
  }

  trackDemandeReparationsById(_index: number, item: IDemandeReparations): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypePanne>>): void {
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

  protected updateForm(typePanne: ITypePanne): void {
    this.editForm.patchValue({
      id: typePanne.id,
      libelleTypePanne: typePanne.libelleTypePanne,
      statutSup: typePanne.statutSup,
      demandeReparations: typePanne.demandeReparations,
    });

    this.demandeReparationsSharedCollection = this.demandeReparationsService.addDemandeReparationsToCollectionIfMissing(
      this.demandeReparationsSharedCollection,
      typePanne.demandeReparations
    );
  }

  protected loadRelationshipsOptions(): void {
    this.demandeReparationsService
      .query()
      .pipe(map((res: HttpResponse<IDemandeReparations[]>) => res.body ?? []))
      .pipe(
        map((demandeReparations: IDemandeReparations[]) =>
          this.demandeReparationsService.addDemandeReparationsToCollectionIfMissing(
            demandeReparations,
            this.editForm.get('demandeReparations')!.value
          )
        )
      )
      .subscribe((demandeReparations: IDemandeReparations[]) => (this.demandeReparationsSharedCollection = demandeReparations));
  }

  protected createFromForm(): ITypePanne {
    return {
      ...new TypePanne(),
      id: this.editForm.get(['id'])!.value,
      libelleTypePanne: this.editForm.get(['libelleTypePanne'])!.value,
      statutSup: this.editForm.get(['statutSup'])!.value,
      demandeReparations: this.editForm.get(['demandeReparations'])!.value,
    };
  }
}
