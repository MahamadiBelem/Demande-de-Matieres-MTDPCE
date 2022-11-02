import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CarnetVehiculeService } from '../service/carnet-vehicule.service';
import { ICarnetVehicule, CarnetVehicule } from '../carnet-vehicule.model';
import { IMarqueVehicule } from 'app/entities/marque-vehicule/marque-vehicule.model';
import { MarqueVehiculeService } from 'app/entities/marque-vehicule/service/marque-vehicule.service';
import { IStructure } from 'app/entities/structure/structure.model';
import { StructureService } from 'app/entities/structure/service/structure.service';

import { CarnetVehiculeUpdateComponent } from './carnet-vehicule-update.component';

describe('CarnetVehicule Management Update Component', () => {
  let comp: CarnetVehiculeUpdateComponent;
  let fixture: ComponentFixture<CarnetVehiculeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let carnetVehiculeService: CarnetVehiculeService;
  let marqueVehiculeService: MarqueVehiculeService;
  let structureService: StructureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CarnetVehiculeUpdateComponent],
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
      .overrideTemplate(CarnetVehiculeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CarnetVehiculeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    carnetVehiculeService = TestBed.inject(CarnetVehiculeService);
    marqueVehiculeService = TestBed.inject(MarqueVehiculeService);
    structureService = TestBed.inject(StructureService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call marqueVehicule query and add missing value', () => {
      const carnetVehicule: ICarnetVehicule = { id: 456 };
      const marqueVehicule: IMarqueVehicule = { id: 88506 };
      carnetVehicule.marqueVehicule = marqueVehicule;

      const marqueVehiculeCollection: IMarqueVehicule[] = [{ id: 71823 }];
      jest.spyOn(marqueVehiculeService, 'query').mockReturnValue(of(new HttpResponse({ body: marqueVehiculeCollection })));
      const expectedCollection: IMarqueVehicule[] = [marqueVehicule, ...marqueVehiculeCollection];
      jest.spyOn(marqueVehiculeService, 'addMarqueVehiculeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ carnetVehicule });
      comp.ngOnInit();

      expect(marqueVehiculeService.query).toHaveBeenCalled();
      expect(marqueVehiculeService.addMarqueVehiculeToCollectionIfMissing).toHaveBeenCalledWith(marqueVehiculeCollection, marqueVehicule);
      expect(comp.marqueVehiculesCollection).toEqual(expectedCollection);
    });

    it('Should call Structure query and add missing value', () => {
      const carnetVehicule: ICarnetVehicule = { id: 456 };
      const structures: IStructure[] = [{ id: 16850 }];
      carnetVehicule.structures = structures;

      const structureCollection: IStructure[] = [{ id: 44130 }];
      jest.spyOn(structureService, 'query').mockReturnValue(of(new HttpResponse({ body: structureCollection })));
      const additionalStructures = [...structures];
      const expectedCollection: IStructure[] = [...additionalStructures, ...structureCollection];
      jest.spyOn(structureService, 'addStructureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ carnetVehicule });
      comp.ngOnInit();

      expect(structureService.query).toHaveBeenCalled();
      expect(structureService.addStructureToCollectionIfMissing).toHaveBeenCalledWith(structureCollection, ...additionalStructures);
      expect(comp.structuresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const carnetVehicule: ICarnetVehicule = { id: 456 };
      const marqueVehicule: IMarqueVehicule = { id: 27314 };
      carnetVehicule.marqueVehicule = marqueVehicule;
      const structures: IStructure = { id: 89824 };
      carnetVehicule.structures = [structures];

      activatedRoute.data = of({ carnetVehicule });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(carnetVehicule));
      expect(comp.marqueVehiculesCollection).toContain(marqueVehicule);
      expect(comp.structuresSharedCollection).toContain(structures);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CarnetVehicule>>();
      const carnetVehicule = { id: 123 };
      jest.spyOn(carnetVehiculeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ carnetVehicule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: carnetVehicule }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(carnetVehiculeService.update).toHaveBeenCalledWith(carnetVehicule);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CarnetVehicule>>();
      const carnetVehicule = new CarnetVehicule();
      jest.spyOn(carnetVehiculeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ carnetVehicule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: carnetVehicule }));
      saveSubject.complete();

      // THEN
      expect(carnetVehiculeService.create).toHaveBeenCalledWith(carnetVehicule);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CarnetVehicule>>();
      const carnetVehicule = { id: 123 };
      jest.spyOn(carnetVehiculeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ carnetVehicule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(carnetVehiculeService.update).toHaveBeenCalledWith(carnetVehicule);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMarqueVehiculeById', () => {
      it('Should return tracked MarqueVehicule primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMarqueVehiculeById(0, entity);
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
    describe('getSelectedStructure', () => {
      it('Should return option if no Structure is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedStructure(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Structure for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedStructure(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Structure is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedStructure(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
