import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypePanneService } from '../service/type-panne.service';
import { ITypePanne, TypePanne } from '../type-panne.model';
import { IDemandeReparations } from 'app/entities/demande-reparations/demande-reparations.model';
import { DemandeReparationsService } from 'app/entities/demande-reparations/service/demande-reparations.service';

import { TypePanneUpdateComponent } from './type-panne-update.component';

describe('TypePanne Management Update Component', () => {
  let comp: TypePanneUpdateComponent;
  let fixture: ComponentFixture<TypePanneUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typePanneService: TypePanneService;
  let demandeReparationsService: DemandeReparationsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TypePanneUpdateComponent],
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
      .overrideTemplate(TypePanneUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypePanneUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typePanneService = TestBed.inject(TypePanneService);
    demandeReparationsService = TestBed.inject(DemandeReparationsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call DemandeReparations query and add missing value', () => {
      const typePanne: ITypePanne = { id: 456 };
      const demandeReparations: IDemandeReparations = { id: 98691 };
      typePanne.demandeReparations = demandeReparations;

      const demandeReparationsCollection: IDemandeReparations[] = [{ id: 94874 }];
      jest.spyOn(demandeReparationsService, 'query').mockReturnValue(of(new HttpResponse({ body: demandeReparationsCollection })));
      const additionalDemandeReparations = [demandeReparations];
      const expectedCollection: IDemandeReparations[] = [...additionalDemandeReparations, ...demandeReparationsCollection];
      jest.spyOn(demandeReparationsService, 'addDemandeReparationsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ typePanne });
      comp.ngOnInit();

      expect(demandeReparationsService.query).toHaveBeenCalled();
      expect(demandeReparationsService.addDemandeReparationsToCollectionIfMissing).toHaveBeenCalledWith(
        demandeReparationsCollection,
        ...additionalDemandeReparations
      );
      expect(comp.demandeReparationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const typePanne: ITypePanne = { id: 456 };
      const demandeReparations: IDemandeReparations = { id: 62211 };
      typePanne.demandeReparations = demandeReparations;

      activatedRoute.data = of({ typePanne });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(typePanne));
      expect(comp.demandeReparationsSharedCollection).toContain(demandeReparations);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypePanne>>();
      const typePanne = { id: 123 };
      jest.spyOn(typePanneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typePanne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typePanne }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(typePanneService.update).toHaveBeenCalledWith(typePanne);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypePanne>>();
      const typePanne = new TypePanne();
      jest.spyOn(typePanneService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typePanne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typePanne }));
      saveSubject.complete();

      // THEN
      expect(typePanneService.create).toHaveBeenCalledWith(typePanne);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypePanne>>();
      const typePanne = { id: 123 };
      jest.spyOn(typePanneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typePanne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typePanneService.update).toHaveBeenCalledWith(typePanne);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDemandeReparationsById', () => {
      it('Should return tracked DemandeReparations primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDemandeReparationsById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
