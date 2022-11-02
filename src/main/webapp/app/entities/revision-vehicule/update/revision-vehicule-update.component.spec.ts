import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RevisionVehiculeService } from '../service/revision-vehicule.service';
import { IRevisionVehicule, RevisionVehicule } from '../revision-vehicule.model';
import { ICarnetVehicule } from 'app/entities/carnet-vehicule/carnet-vehicule.model';
import { CarnetVehiculeService } from 'app/entities/carnet-vehicule/service/carnet-vehicule.service';

import { RevisionVehiculeUpdateComponent } from './revision-vehicule-update.component';

describe('RevisionVehicule Management Update Component', () => {
  let comp: RevisionVehiculeUpdateComponent;
  let fixture: ComponentFixture<RevisionVehiculeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let revisionVehiculeService: RevisionVehiculeService;
  let carnetVehiculeService: CarnetVehiculeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RevisionVehiculeUpdateComponent],
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
      .overrideTemplate(RevisionVehiculeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RevisionVehiculeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    revisionVehiculeService = TestBed.inject(RevisionVehiculeService);
    carnetVehiculeService = TestBed.inject(CarnetVehiculeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CarnetVehicule query and add missing value', () => {
      const revisionVehicule: IRevisionVehicule = { id: 456 };
      const carnetVehicule: ICarnetVehicule = { id: 8249 };
      revisionVehicule.carnetVehicule = carnetVehicule;

      const carnetVehiculeCollection: ICarnetVehicule[] = [{ id: 19865 }];
      jest.spyOn(carnetVehiculeService, 'query').mockReturnValue(of(new HttpResponse({ body: carnetVehiculeCollection })));
      const additionalCarnetVehicules = [carnetVehicule];
      const expectedCollection: ICarnetVehicule[] = [...additionalCarnetVehicules, ...carnetVehiculeCollection];
      jest.spyOn(carnetVehiculeService, 'addCarnetVehiculeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ revisionVehicule });
      comp.ngOnInit();

      expect(carnetVehiculeService.query).toHaveBeenCalled();
      expect(carnetVehiculeService.addCarnetVehiculeToCollectionIfMissing).toHaveBeenCalledWith(
        carnetVehiculeCollection,
        ...additionalCarnetVehicules
      );
      expect(comp.carnetVehiculesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const revisionVehicule: IRevisionVehicule = { id: 456 };
      const carnetVehicule: ICarnetVehicule = { id: 78132 };
      revisionVehicule.carnetVehicule = carnetVehicule;

      activatedRoute.data = of({ revisionVehicule });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(revisionVehicule));
      expect(comp.carnetVehiculesSharedCollection).toContain(carnetVehicule);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RevisionVehicule>>();
      const revisionVehicule = { id: 123 };
      jest.spyOn(revisionVehiculeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ revisionVehicule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: revisionVehicule }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(revisionVehiculeService.update).toHaveBeenCalledWith(revisionVehicule);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RevisionVehicule>>();
      const revisionVehicule = new RevisionVehicule();
      jest.spyOn(revisionVehiculeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ revisionVehicule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: revisionVehicule }));
      saveSubject.complete();

      // THEN
      expect(revisionVehiculeService.create).toHaveBeenCalledWith(revisionVehicule);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RevisionVehicule>>();
      const revisionVehicule = { id: 123 };
      jest.spyOn(revisionVehiculeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ revisionVehicule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(revisionVehiculeService.update).toHaveBeenCalledWith(revisionVehicule);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCarnetVehiculeById', () => {
      it('Should return tracked CarnetVehicule primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCarnetVehiculeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
