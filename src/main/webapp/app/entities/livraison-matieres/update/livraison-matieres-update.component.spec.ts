import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LivraisonMatieresService } from '../service/livraison-matieres.service';
import { ILivraisonMatieres, LivraisonMatieres } from '../livraison-matieres.model';
import { IMatieres } from 'app/entities/matieres/matieres.model';
import { MatieresService } from 'app/entities/matieres/service/matieres.service';
import { IStructure } from 'app/entities/structure/structure.model';
import { StructureService } from 'app/entities/structure/service/structure.service';

import { LivraisonMatieresUpdateComponent } from './livraison-matieres-update.component';

describe('LivraisonMatieres Management Update Component', () => {
  let comp: LivraisonMatieresUpdateComponent;
  let fixture: ComponentFixture<LivraisonMatieresUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let livraisonMatieresService: LivraisonMatieresService;
  let matieresService: MatieresService;
  let structureService: StructureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LivraisonMatieresUpdateComponent],
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
      .overrideTemplate(LivraisonMatieresUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LivraisonMatieresUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    livraisonMatieresService = TestBed.inject(LivraisonMatieresService);
    matieresService = TestBed.inject(MatieresService);
    structureService = TestBed.inject(StructureService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Matieres query and add missing value', () => {
      const livraisonMatieres: ILivraisonMatieres = { id: 456 };
      const matieres: IMatieres[] = [{ id: 90423 }];
      livraisonMatieres.matieres = matieres;

      const matieresCollection: IMatieres[] = [{ id: 88370 }];
      jest.spyOn(matieresService, 'query').mockReturnValue(of(new HttpResponse({ body: matieresCollection })));
      const additionalMatieres = [...matieres];
      const expectedCollection: IMatieres[] = [...additionalMatieres, ...matieresCollection];
      jest.spyOn(matieresService, 'addMatieresToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ livraisonMatieres });
      comp.ngOnInit();

      expect(matieresService.query).toHaveBeenCalled();
      expect(matieresService.addMatieresToCollectionIfMissing).toHaveBeenCalledWith(matieresCollection, ...additionalMatieres);
      expect(comp.matieresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Structure query and add missing value', () => {
      const livraisonMatieres: ILivraisonMatieres = { id: 456 };
      const structure: IStructure = { id: 62309 };
      livraisonMatieres.structure = structure;

      const structureCollection: IStructure[] = [{ id: 54746 }];
      jest.spyOn(structureService, 'query').mockReturnValue(of(new HttpResponse({ body: structureCollection })));
      const additionalStructures = [structure];
      const expectedCollection: IStructure[] = [...additionalStructures, ...structureCollection];
      jest.spyOn(structureService, 'addStructureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ livraisonMatieres });
      comp.ngOnInit();

      expect(structureService.query).toHaveBeenCalled();
      expect(structureService.addStructureToCollectionIfMissing).toHaveBeenCalledWith(structureCollection, ...additionalStructures);
      expect(comp.structuresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const livraisonMatieres: ILivraisonMatieres = { id: 456 };
      const matieres: IMatieres = { id: 95985 };
      livraisonMatieres.matieres = [matieres];
      const structure: IStructure = { id: 59295 };
      livraisonMatieres.structure = structure;

      activatedRoute.data = of({ livraisonMatieres });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(livraisonMatieres));
      expect(comp.matieresSharedCollection).toContain(matieres);
      expect(comp.structuresSharedCollection).toContain(structure);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LivraisonMatieres>>();
      const livraisonMatieres = { id: 123 };
      jest.spyOn(livraisonMatieresService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ livraisonMatieres });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: livraisonMatieres }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(livraisonMatieresService.update).toHaveBeenCalledWith(livraisonMatieres);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LivraisonMatieres>>();
      const livraisonMatieres = new LivraisonMatieres();
      jest.spyOn(livraisonMatieresService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ livraisonMatieres });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: livraisonMatieres }));
      saveSubject.complete();

      // THEN
      expect(livraisonMatieresService.create).toHaveBeenCalledWith(livraisonMatieres);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LivraisonMatieres>>();
      const livraisonMatieres = { id: 123 };
      jest.spyOn(livraisonMatieresService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ livraisonMatieres });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(livraisonMatieresService.update).toHaveBeenCalledWith(livraisonMatieres);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMatieresById', () => {
      it('Should return tracked Matieres primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMatieresById(0, entity);
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
    describe('getSelectedMatieres', () => {
      it('Should return option if no Matieres is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedMatieres(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Matieres for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedMatieres(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Matieres is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedMatieres(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
