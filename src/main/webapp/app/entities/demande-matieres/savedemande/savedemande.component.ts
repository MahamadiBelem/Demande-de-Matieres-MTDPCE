import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { EventManager } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DataUtils } from 'app/core/util/data-util.service';
import { StructureService } from 'app/entities/structure/service/structure.service';
import dayjs from 'dayjs';
import { DemandeMatieres, IDemandeMatieres } from '../demande-matieres.model';
import { DemandeMatieresService } from '../service/demande-matieres.service';

@Component({
  selector: 'jhi-savedemande',
  templateUrl: './savedemande.component.html',
  styleUrls: ['./savedemande.component.scss'],
})
export class SavedemandeComponent implements OnInit {
  editForm: any;
  //activatedRoute: any;

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected demandeMatieresService: DemandeMatieresService,
    protected structureService: StructureService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    //test
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demandeMatieres }): void => {
      if (demandeMatieres.id === undefined) {
        const today = dayjs().startOf('day');
        demandeMatieres.date = today;
      }
    });
  }

  protected createFromForm(): IDemandeMatieres {
    return {
      ...new DemandeMatieres(),
      id: this.editForm.get(['id'])!.value,
      // date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
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
