import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDemandeReparations, DemandeReparations } from '../demande-reparations.model';
import { DemandeReparationsService } from '../service/demande-reparations.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ITypeMatiere } from 'app/entities/type-matiere/type-matiere.model';
import { TypeMatiereService } from 'app/entities/type-matiere/service/type-matiere.service';
import { IStructure } from 'app/entities/structure/structure.model';
import { StructureService } from 'app/entities/structure/service/structure.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { StructureComponent } from 'app/entities/structure/list/structure.component';
import { StructureUpdateComponent } from 'app/entities/structure/update/structure-update.component';
import { MatieresUpdateComponent } from 'app/entities/matieres/update/matieres-update.component';
//import { SaveReparationsComponent } from '../save-reparations/save-reparations.component';

@Component({
  selector: 'jhi-demande-reparations-update',
  templateUrl: './demande-reparations-update.component.html',
})
export class DemandeReparationsUpdateComponent implements OnInit {
  isSaving = false;

  typeMatieresSharedCollection: ITypeMatiere[] = [];
  structuresSharedCollection: IStructure[] = [];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    indentiteSoumettant: [null, [Validators.required, Validators.minLength(5)]],
    fonction: [null, [Validators.required, Validators.minLength(3)]],
    designation: [null, [Validators.required, Validators.minLength(3)]],
    observation: [],
    statutSup: [],
    typeMatieres: [],
    structure: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected demandeReparationsService: DemandeReparationsService,
    protected typeMatiereService: TypeMatiereService,
    protected structureService: StructureService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demandeReparations }) => {
      if (demandeReparations.id === undefined) {
        const today = dayjs().startOf('day');
        demandeReparations.date = today;
      }

      this.updateForm(demandeReparations);

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
    const demandeReparations = this.createFromForm();
    if (demandeReparations.id !== undefined) {
      this.subscribeToSaveResponse(this.demandeReparationsService.update(demandeReparations));
    } else {
      this.subscribeToSaveResponse(this.demandeReparationsService.create(demandeReparations));
    }
  }

  trackTypeMatiereById(_index: number, item: ITypeMatiere): number {
    return item.id!;
  }

  trackStructureById(_index: number, item: IStructure): number {
    return item.id!;
  }

  getSelectedTypeMatiere(option: ITypeMatiere, selectedVals?: ITypeMatiere[]): ITypeMatiere {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  savemodale(): void {
    const savemodal = this.modalService.open(MatieresUpdateComponent, { size: 'lg', backdrop: 'static' });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDemandeReparations>>): void {
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

  protected updateForm(demandeReparations: IDemandeReparations): void {
    this.editForm.patchValue({
      id: demandeReparations.id,
      date: demandeReparations.date ? demandeReparations.date.format(DATE_TIME_FORMAT) : null,
      indentiteSoumettant: demandeReparations.indentiteSoumettant,
      fonction: demandeReparations.fonction,
      designation: demandeReparations.designation,
      observation: demandeReparations.observation,
      statutSup: demandeReparations.statutSup,
      typeMatieres: demandeReparations.typeMatieres,
      structure: demandeReparations.structure,
    });

    this.typeMatieresSharedCollection = this.typeMatiereService.addTypeMatiereToCollectionIfMissing(
      this.typeMatieresSharedCollection,
      ...(demandeReparations.typeMatieres ?? [])
    );
    this.structuresSharedCollection = this.structureService.addStructureToCollectionIfMissing(
      this.structuresSharedCollection,
      demandeReparations.structure
    );
  }

  protected loadRelationshipsOptions(): void {
    this.typeMatiereService
      .query()
      .pipe(map((res: HttpResponse<ITypeMatiere[]>) => res.body ?? []))
      .pipe(
        map((typeMatieres: ITypeMatiere[]) =>
          this.typeMatiereService.addTypeMatiereToCollectionIfMissing(typeMatieres, ...(this.editForm.get('typeMatieres')!.value ?? []))
        )
      )
      .subscribe((typeMatieres: ITypeMatiere[]) => (this.typeMatieresSharedCollection = typeMatieres));

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

  protected createFromForm(): IDemandeReparations {
    return {
      ...new DemandeReparations(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      indentiteSoumettant: this.editForm.get(['indentiteSoumettant'])!.value,
      fonction: this.editForm.get(['fonction'])!.value,
      designation: this.editForm.get(['designation'])!.value,
      observation: this.editForm.get(['observation'])!.value,
      statutSup: this.editForm.get(['statutSup'])!.value,
      typeMatieres: this.editForm.get(['typeMatieres'])!.value,
      structure: this.editForm.get(['structure'])!.value,
    };
  }
}
