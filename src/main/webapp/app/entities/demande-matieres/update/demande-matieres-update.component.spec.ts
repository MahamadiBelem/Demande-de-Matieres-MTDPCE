import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DemandeMatieresService } from '../service/demande-matieres.service';
import { IDemandeMatieres, DemandeMatieres } from '../demande-matieres.model';
import { IStructure } from 'app/entities/structure/structure.model';
import { StructureService } from 'app/entities/structure/service/structure.service';

import { DemandeMatieresUpdateComponent } from './demande-matieres-update.component';

describe('DemandeMatieres Management Update Component', () => {
  let comp: DemandeMatieresUpdateComponent;
  let fixture: ComponentFixture<DemandeMatieresUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let demandeMatieresService: DemandeMatieresService;
  let structureService: StructureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DemandeMatieresUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DemandeMatieresUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DemandeMatieresUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    demandeMatieresService = TestBed.inject(DemandeMatieresService);
    structureService = TestBed.inject(StructureService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Structure query and add missing value', () => {
      const demandeMatieres: IDemandeMatieres = { id: 456 };
      const structure: IStructure = { id: 16808 };
      demandeMatieres.structure = structure;

      const structureCollection: IStructure[] = [{ id: 51872 }];
      jest.spyOn(structureService, 'query').mockReturnValue(of(new HttpResponse({ body: structureCollection })));
      const additionalStructures = [structure];
      const expectedCollection: IStructure[] = [...additionalStructures, ...structureCollection];
      jest.spyOn(structureService, 'addStructureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demandeMatieres });
      comp.ngOnInit();

      expect(structureService.query).toHaveBeenCalled();
      expect(structureService.addStructureToCollectionIfMissing).toHaveBeenCalledWith(structureCollection, ...additionalStructures);
      expect(comp.structuresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const demandeMatieres: IDemandeMatieres = { id: 456 };
      const structure: IStructure = { id: 52910 };
      demandeMatieres.structure = structure;

      activatedRoute.data = of({ demandeMatieres });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(demandeMatieres));
      expect(comp.structuresSharedCollection).toContain(structure);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeMatieres>>();
      const demandeMatieres = { id: 123 };
      jest.spyOn(demandeMatieresService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeMatieres });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demandeMatieres }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(demandeMatieresService.update).toHaveBeenCalledWith(demandeMatieres);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeMatieres>>();
      const demandeMatieres = new DemandeMatieres();
      jest.spyOn(demandeMatieresService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeMatieres });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demandeMatieres }));
      saveSubject.complete();

      // THEN
      expect(demandeMatieresService.create).toHaveBeenCalledWith(demandeMatieres);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeMatieres>>();
      const demandeMatieres = { id: 123 };
      jest.spyOn(demandeMatieresService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeMatieres });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(demandeMatieresService.update).toHaveBeenCalledWith(demandeMatieres);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackStructureById', () => {
      it('Should return tracked Structure primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackStructureById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
