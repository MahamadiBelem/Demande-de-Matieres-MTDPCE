import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITypeMatiere, TypeMatiere } from '../type-matiere.model';
import { TypeMatiereService } from '../service/type-matiere.service';

@Component({
  selector: 'jhi-type-matiere-update',
  templateUrl: './type-matiere-update.component.html',
})
export class TypeMatiereUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    libelleTypeMatiere: [null, [Validators.required, Validators.minLength(3)]],
  });

  constructor(protected typeMatiereService: TypeMatiereService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeMatiere }) => {
      this.updateForm(typeMatiere);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeMatiere = this.createFromForm();
    if (typeMatiere.id !== undefined) {
      this.subscribeToSaveResponse(this.typeMatiereService.update(typeMatiere));
    } else {
      this.subscribeToSaveResponse(this.typeMatiereService.create(typeMatiere));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeMatiere>>): void {
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

  protected updateForm(typeMatiere: ITypeMatiere): void {
    this.editForm.patchValue({
      id: typeMatiere.id,
      libelleTypeMatiere: typeMatiere.libelleTypeMatiere,
    });
  }

  protected createFromForm(): ITypeMatiere {
    return {
      ...new TypeMatiere(),
      id: this.editForm.get(['id'])!.value,
      libelleTypeMatiere: this.editForm.get(['libelleTypeMatiere'])!.value,
    };
  }
}
