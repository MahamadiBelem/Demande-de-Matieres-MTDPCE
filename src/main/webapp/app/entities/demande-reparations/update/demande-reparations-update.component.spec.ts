import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DemandeReparationsService } from '../service/demande-reparations.service';
import { IDemandeReparations, DemandeReparations } from '../demande-reparations.model';
import { ITypeMatiere } from 'app/entities/type-matiere/type-matiere.model';
import { TypeMatiereService } from 'app/entities/type-matiere/service/type-matiere.service';
import { IStructure } from 'app/entities/structure/structure.model';
import { StructureService } from 'app/entities/structure/service/structure.service';

import { DemandeReparationsUpdateComponent } from './demande-reparations-update.component';

describe('DemandeReparations Management Update Component', () => {
  let comp: DemandeReparationsUpdateComponent;
  let fixture: ComponentFixture<DemandeReparationsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let demandeReparationsService: DemandeReparationsService;
  let typeMatiereService: TypeMatiereService;
  let structureService: StructureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DemandeReparationsUpdateComponent],
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
      .overrideTemplate(DemandeReparationsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DemandeReparationsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    demandeReparationsService = TestBed.inject(DemandeReparationsService);
    typeMatiereService = TestBed.inject(TypeMatiereService);
    structureService = TestBed.inject(StructureService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TypeMatiere query and add missing value', () => {
      const demandeReparations: IDemandeReparations = { id: 456 };
      const typeMatieres: ITypeMatiere[] = [{ id: 64709 }];
      demandeReparations.typeMatieres = typeMatieres;

      const typeMatiereCollection: ITypeMatiere[] = [{ id: 61471 }];
      jest.spyOn(typeMatiereService, 'query').mockReturnValue(of(new HttpResponse({ body: typeMatiereCollection })));
      const additionalTypeMatieres = [...typeMatieres];
      const expectedCollection: ITypeMatiere[] = [...additionalTypeMatieres, ...typeMatiereCollection];
      jest.spyOn(typeMatiereService, 'addTypeMatiereToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demandeReparations });
      comp.ngOnInit();

      expect(typeMatiereService.query).toHaveBeenCalled();
      expect(typeMatiereService.addTypeMatiereToCollectionIfMissing).toHaveBeenCalledWith(typeMatiereCollection, ...additionalTypeMatieres);
      expect(comp.typeMatieresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Structure query and add missing value', () => {
      const demandeReparations: IDemandeReparations = { id: 456 };
      const structure: IStructure = { id: 98636 };
      demandeReparations.structure = structure;

      const structureCollection: IStructure[] = [{ id: 45581 }];
      jest.spyOn(structureService, 'query').mockReturnValue(of(new HttpResponse({ body: structureCollection })));
      const additionalStructures = [structure];
      const expectedCollection: IStructure[] = [...additionalStructures, ...structureCollection];
      jest.spyOn(structureService, 'addStructureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demandeReparations });
      comp.ngOnInit();

      expect(structureService.query).toHaveBeenCalled();
      expect(structureService.addStructureToCollectionIfMissing).toHaveBeenCalledWith(structureCollection, ...additionalStructures);
      expect(comp.structuresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const demandeReparations: IDemandeReparations = { id: 456 };
      const typeMatieres: ITypeMatiere = { id: 10237 };
      demandeReparations.typeMatieres = [typeMatieres];
      const structure: IStructure = { id: 26348 };
      demandeReparations.structure = structure;

      activatedRoute.data = of({ demandeReparations });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(demandeReparations));
      expect(comp.typeMatieresSharedCollection).toContain(typeMatieres);
      expect(comp.structuresSharedCollection).toContain(structure);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeReparations>>();
      const demandeReparations = { id: 123 };
      jest.spyOn(demandeReparationsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeReparations });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demandeReparations }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(demandeReparationsService.update).toHaveBeenCalledWith(demandeReparations);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeReparations>>();
      const demandeReparations = new DemandeReparations();
      jest.spyOn(demandeReparationsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeReparations });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demandeReparations }));
      saveSubject.complete();

      // THEN
      expect(demandeReparationsService.create).toHaveBeenCalledWith(demandeReparations);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeReparations>>();
      const demandeReparations = { id: 123 };
      jest.spyOn(demandeReparationsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeReparations });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(demandeReparationsService.update).toHaveBeenCalledWith(demandeReparations);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTypeMatiereById', () => {
      it('Should return tracked TypeMatiere primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTypeMatiereById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackStructureById', () => {
      it('Should return tracked Structure primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackStructureById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedTypeMatiere', () => {
      it('Should return option if no TypeMatiere is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedTypeMatiere(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected TypeMatiere for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedTypeMatiere(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this TypeMatiere is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedTypeMatiere(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
