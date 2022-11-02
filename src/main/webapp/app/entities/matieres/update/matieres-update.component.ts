import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMatieres, Matieres } from '../matieres.model';
import { MatieresService } from '../service/matieres.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-matieres-update',
  templateUrl: './matieres-update.component.html',
})
export class MatieresUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    designationMatieres: [null, [Validators.required, Validators.minLength(3)]],
    quantiteMatieres: [null, [Validators.required]],
    caracteristiquesMatieres: [],
    statutSup: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected matieresService: MatieresService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ matieres }) => {
      this.updateForm(matieres);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('demandeMatieresApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const matieres = this.createFromForm();
    if (matieres.id !== undefined) {
      this.subscribeToSaveResponse(this.matieresService.update(matieres));
    } else {
      this.subscribeToSaveResponse(this.matieresService.create(matieres));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatieres>>): void {
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

  protected updateForm(matieres: IMatieres): void {
    this.editForm.patchValue({
      id: matieres.id,
      designationMatieres: matieres.designationMatieres,
      quantiteMatieres: matieres.quantiteMatieres,
      caracteristiquesMatieres: matieres.caracteristiquesMatieres,
      statutSup: matieres.statutSup,
    });
  }

  protected createFromForm(): IMatieres {
    return {
      ...new Matieres(),
      id: this.editForm.get(['id'])!.value,
      designationMatieres: this.editForm.get(['designationMatieres'])!.value,
      quantiteMatieres: this.editForm.get(['quantiteMatieres'])!.value,
      caracteristiquesMatieres: this.editForm.get(['caracteristiquesMatieres'])!.value,
      statutSup: this.editForm.get(['statutSup'])!.value,
    };
  }
}
