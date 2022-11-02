import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDemandeMatieres, DemandeMatieres } from '../demande-matieres.model';
import { DemandeMatieresService } from '../service/demande-matieres.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IStructure } from 'app/entities/structure/structure.model';
import { StructureService } from 'app/entities/structure/service/structure.service';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-demande-matieres-update',
  templateUrl: './demande-matieres-update.component.html',
})
export class DemandeMatieresUpdateComponent implements OnInit {
  isSaving = false;

  structuresSharedCollection: IStructure[] = [];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    indentiteSoumettant: [null, [Validators.required, Validators.minLength(5)]],
    fonction: [null, [Validators.required, Validators.minLength(3)]],
    designation: [null, [Validators.required, Validators.minLength(3)]],
    quantite: [null, [Validators.required]],
    observation: [],
    statutSup: [],
    structure: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected demandeMatieresService: DemandeMatieresService,
    protected structureService: StructureService,
    private activemodale: NgbActiveModal,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    //test
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demandeMatieres }) => {
      if (demandeMatieres.id === undefined) {
        const today = dayjs().startOf('day');
        demandeMatieres.date = today;
      }

      this.updateForm(demandeMatieres);

      this.loadRelationshipsOptions();
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
    const demandeMatieres = this.createFromForm();
    if (demandeMatieres.id !== undefined) {
      this.subscribeToSaveResponse(this.demandeMatieresService.update(demandeMatieres));
    } else {
      this.subscribeToSaveResponse(this.demandeMatieresService.create(demandeMatieres));
    }
  }

  trackStructureById(_index: number, item: IStructure): number {
    return item.id!;
  }
  cancel(): void {
    this.activemodale.dismiss();
  }
  // savemodale(): void {
  // const savemodal = this.modalService.open(DemandeMatieresUpdateComponent, { size: 'lg', backdrop: 'static' });
  //}
  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDemandeMatieres>>): void {
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

  protected updateForm(demandeMatieres: IDemandeMatieres): void {
    this.editForm.patchValue({
      id: demandeMatieres.id,
      date: demandeMatieres.date ? demandeMatieres.date.format(DATE_TIME_FORMAT) : null,
      indentiteSoumettant: demandeMatieres.indentiteSoumettant,
      fonction: demandeMatieres.fonction,
      designation: demandeMatieres.designation,
      quantite: demandeMatieres.quantite,
      observation: demandeMatieres.observation,
      statutSup: demandeMatieres.statutSup,
      structure: demandeMatieres.structure,
    });

    this.structuresSharedCollection = this.structureService.addStructureToCollectionIfMissing(
      this.structuresSharedCollection,
      demandeMatieres.structure
    );
  }

  protected loadRelationshipsOptions(): void {
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

  protected createFromForm(): IDemandeMatieres {
    return {
      ...new DemandeMatieres(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      indentiteSoumettant: this.editForm.get(['indentiteSoumettant'])!.value,
      fonction: this.editForm.get(['fonction'])!.value,
      designation: this.editForm.get(['designation'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      observation: this.editForm.get(['observation'])!.value,
      statutSup: this.editForm.get(['statutSup'])!.value,
      structure: this.editForm.get(['structure'])!.value,
    };
  }
}
