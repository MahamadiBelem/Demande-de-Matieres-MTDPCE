import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IStructure, Structure } from '../structure.model';
import { StructureService } from '../service/structure.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-structure-update',
  templateUrl: './structure-update.component.html',
})
export class StructureUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    libelleStructure: [null, [Validators.required, Validators.minLength(2)]],
    codeStructure: [null, [Validators.required, Validators.minLength(2), Validators.pattern('^[a-zA-Z0-9]*$')]],
  });

  constructor(
    protected structureService: StructureService,
    protected activatedRoute: ActivatedRoute,
    private activemodale: NgbActiveModal,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ structure }) => {
      this.updateForm(structure);
    });
  }

  previousState(): void {
    window.history.back();
  }

  // test
  cancel(): void {
    this.activemodale.dismiss();
  }

  save(): void {
    this.isSaving = true;
    const structure = this.createFromForm();
    if (structure.id !== undefined) {
      this.subscribeToSaveResponse(this.structureService.update(structure));
    } else {
      this.subscribeToSaveResponse(this.structureService.create(structure));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStructure>>): void {
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

  protected updateForm(structure: IStructure): void {
    this.editForm.patchValue({
      id: structure.id,
      libelleStructure: structure.libelleStructure,
      codeStructure: structure.codeStructure,
    });
  }

  protected createFromForm(): IStructure {
    return {
      ...new Structure(),
      id: this.editForm.get(['id'])!.value,
      libelleStructure: this.editForm.get(['libelleStructure'])!.value,
      codeStructure: this.editForm.get(['codeStructure'])!.value,
    };
  }
}
