import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICarnetVehicule, CarnetVehicule } from '../carnet-vehicule.model';
import { CarnetVehiculeService } from '../service/carnet-vehicule.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IMarqueVehicule } from 'app/entities/marque-vehicule/marque-vehicule.model';
import { MarqueVehiculeService } from 'app/entities/marque-vehicule/service/marque-vehicule.service';
import { IStructure } from 'app/entities/structure/structure.model';
import { StructureService } from 'app/entities/structure/service/structure.service';
import { Etatvehicule } from 'app/entities/enumerations/etatvehicule.model';

@Component({
  selector: 'jhi-carnet-vehicule-update',
  templateUrl: './carnet-vehicule-update.component.html',
})
export class CarnetVehiculeUpdateComponent implements OnInit {
  isSaving = false;
  etatvehiculeValues = Object.keys(Etatvehicule);

  marqueVehiculesCollection: IMarqueVehicule[] = [];
  structuresSharedCollection: IStructure[] = [];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    immatriculationVehicule: [null, [Validators.required, Validators.minLength(3)]],
    identiteConducteur: [null, [Validators.required]],
    nombreReparation: [],
    dateDerniereRevision: [],
    etatvehicule: [],
    observations: [],
    statutSup: [],
    marqueVehicule: [],
    structures: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected carnetVehiculeService: CarnetVehiculeService,
    protected marqueVehiculeService: MarqueVehiculeService,
    protected structureService: StructureService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ carnetVehicule }) => {
      if (carnetVehicule.id === undefined) {
        const today = dayjs().startOf('day');
        carnetVehicule.date = today;
      }

      this.updateForm(carnetVehicule);

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
    const carnetVehicule = this.createFromForm();
    if (carnetVehicule.id !== undefined) {
      this.subscribeToSaveResponse(this.carnetVehiculeService.update(carnetVehicule));
    } else {
      this.subscribeToSaveResponse(this.carnetVehiculeService.create(carnetVehicule));
    }
  }

  trackMarqueVehiculeById(_index: number, item: IMarqueVehicule): number {
    return item.id!;
  }

  trackStructureById(_index: number, item: IStructure): number {
    return item.id!;
  }

  getSelectedStructure(option: IStructure, selectedVals?: IStructure[]): IStructure {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICarnetVehicule>>): void {
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

  protected updateForm(carnetVehicule: ICarnetVehicule): void {
    this.editForm.patchValue({
      id: carnetVehicule.id,
      date: carnetVehicule.date ? carnetVehicule.date.format(DATE_TIME_FORMAT) : null,
      immatriculationVehicule: carnetVehicule.immatriculationVehicule,
      identiteConducteur: carnetVehicule.identiteConducteur,
      nombreReparation: carnetVehicule.nombreReparation,
      dateDerniereRevision: carnetVehicule.dateDerniereRevision,
      etatvehicule: carnetVehicule.etatvehicule,
      observations: carnetVehicule.observations,
      statutSup: carnetVehicule.statutSup,
      marqueVehicule: carnetVehicule.marqueVehicule,
      structures: carnetVehicule.structures,
    });

    this.marqueVehiculesCollection = this.marqueVehiculeService.addMarqueVehiculeToCollectionIfMissing(
      this.marqueVehiculesCollection,
      carnetVehicule.marqueVehicule
    );
    this.structuresSharedCollection = this.structureService.addStructureToCollectionIfMissing(
      this.structuresSharedCollection,
      ...(carnetVehicule.structures ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.marqueVehiculeService
      .query({ filter: 'carnetvehicule-is-null' })
      .pipe(map((res: HttpResponse<IMarqueVehicule[]>) => res.body ?? []))
      .pipe(
        map((marqueVehicules: IMarqueVehicule[]) =>
          this.marqueVehiculeService.addMarqueVehiculeToCollectionIfMissing(marqueVehicules, this.editForm.get('marqueVehicule')!.value)
        )
      )
      .subscribe((marqueVehicules: IMarqueVehicule[]) => (this.marqueVehiculesCollection = marqueVehicules));

    this.structureService
      .query()
      .pipe(map((res: HttpResponse<IStructure[]>) => res.body ?? []))
      .pipe(
        map((structures: IStructure[]) =>
          this.structureService.addStructureToCollectionIfMissing(structures, ...(this.editForm.get('structures')!.value ?? []))
        )
      )
      .subscribe((structures: IStructure[]) => (this.structuresSharedCollection = structures));
  }

  protected createFromForm(): ICarnetVehicule {
    return {
      ...new CarnetVehicule(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      immatriculationVehicule: this.editForm.get(['immatriculationVehicule'])!.value,
      identiteConducteur: this.editForm.get(['identiteConducteur'])!.value,
      nombreReparation: this.editForm.get(['nombreReparation'])!.value,
      dateDerniereRevision: this.editForm.get(['dateDerniereRevision'])!.value,
      etatvehicule: this.editForm.get(['etatvehicule'])!.value,
      observations: this.editForm.get(['observations'])!.value,
      statutSup: this.editForm.get(['statutSup'])!.value,
      marqueVehicule: this.editForm.get(['marqueVehicule'])!.value,
      structures: this.editForm.get(['structures'])!.value,
    };
  }
}
