import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILivraisonMatieres, LivraisonMatieres } from '../livraison-matieres.model';
import { LivraisonMatieresService } from '../service/livraison-matieres.service';
import { IMatieres } from 'app/entities/matieres/matieres.model';
import { MatieresService } from 'app/entities/matieres/service/matieres.service';
import { IStructure } from 'app/entities/structure/structure.model';
import { StructureService } from 'app/entities/structure/service/structure.service';

@Component({
  selector: 'jhi-livraison-matieres-update',
  templateUrl: './livraison-matieres-update.component.html',
})
export class LivraisonMatieresUpdateComponent implements OnInit {
  isSaving = false;

  matieresSharedCollection: IMatieres[] = [];
  structuresSharedCollection: IStructure[] = [];

  editForm = this.fb.group({
    id: [],
    designationMatiere: [null, [Validators.required, Validators.minLength(3)]],
    quantiteLivree: [null, [Validators.required]],
    dateLivree: [],
    statutSup: [],
    matieres: [],
    structure: [],
  });

  constructor(
    protected livraisonMatieresService: LivraisonMatieresService,
    protected matieresService: MatieresService,
    protected structureService: StructureService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ livraisonMatieres }) => {
      if (livraisonMatieres.id === undefined) {
        const today = dayjs().startOf('day');
        livraisonMatieres.dateLivree = today;
      }

      this.updateForm(livraisonMatieres);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const livraisonMatieres = this.createFromForm();
    if (livraisonMatieres.id !== undefined) {
      this.subscribeToSaveResponse(this.livraisonMatieresService.update(livraisonMatieres));
    } else {
      this.subscribeToSaveResponse(this.livraisonMatieresService.create(livraisonMatieres));
    }
  }

  trackMatieresById(_index: number, item: IMatieres): number {
    return item.id!;
  }

  trackStructureById(_index: number, item: IStructure): number {
    return item.id!;
  }

  getSelectedMatieres(option: IMatieres, selectedVals?: IMatieres[]): IMatieres {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILivraisonMatieres>>): void {
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

  protected updateForm(livraisonMatieres: ILivraisonMatieres): void {
    this.editForm.patchValue({
      id: livraisonMatieres.id,
      designationMatiere: livraisonMatieres.designationMatiere,
      quantiteLivree: livraisonMatieres.quantiteLivree,
      dateLivree: livraisonMatieres.dateLivree ? livraisonMatieres.dateLivree.format(DATE_TIME_FORMAT) : null,
      statutSup: livraisonMatieres.statutSup,
      matieres: livraisonMatieres.matieres,
      structure: livraisonMatieres.structure,
    });

    this.matieresSharedCollection = this.matieresService.addMatieresToCollectionIfMissing(
      this.matieresSharedCollection,
      ...(livraisonMatieres.matieres ?? [])
    );
    this.structuresSharedCollection = this.structureService.addStructureToCollectionIfMissing(
      this.structuresSharedCollection,
      livraisonMatieres.structure
    );
  }

  protected loadRelationshipsOptions(): void {
    this.matieresService
      .query()
      .pipe(map((res: HttpResponse<IMatieres[]>) => res.body ?? []))
      .pipe(
        map((matieres: IMatieres[]) =>
          this.matieresService.addMatieresToCollectionIfMissing(matieres, ...(this.editForm.get('matieres')!.value ?? []))
        )
      )
      .subscribe((matieres: IMatieres[]) => (this.matieresSharedCollection = matieres));

    this.structureService
      .query()
      .pipe(map((res: HttpResponse<IStructure[]>) => res.body ?? []))
      .pipe(
        map((structures: IStructure[]) =>
          this.structureService.addStructureToCollectionIfMissing(structures, this.editForm.get('structure')!.value)
        )
      )
      .subscribe((structures: IStructure[]) => (this.structuresSharedCollection = structures));
  }

  protected createFromForm(): ILivraisonMatieres {
    return {
      ...new LivraisonMatieres(),
      id: this.editForm.get(['id'])!.value,
      designationMatiere: this.editForm.get(['designationMatiere'])!.value,
      quantiteLivree: this.editForm.get(['quantiteLivree'])!.value,
      dateLivree: this.editForm.get(['dateLivree'])!.value ? dayjs(this.editForm.get(['dateLivree'])!.value, DATE_TIME_FORMAT) : undefined,
      statutSup: this.editForm.get(['statutSup'])!.value,
      matieres: this.editForm.get(['matieres'])!.value,
      structure: this.editForm.get(['structure'])!.value,
    };
  }
}
