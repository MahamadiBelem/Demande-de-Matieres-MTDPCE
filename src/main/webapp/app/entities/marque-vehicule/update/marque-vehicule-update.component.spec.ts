import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MarqueVehiculeService } from '../service/marque-vehicule.service';
import { IMarqueVehicule, MarqueVehicule } from '../marque-vehicule.model';

import { MarqueVehiculeUpdateComponent } from './marque-vehicule-update.component';

describe('MarqueVehicule Management Update Component', () => {
  let comp: MarqueVehiculeUpdateComponent;
  let fixture: ComponentFixture<MarqueVehiculeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let marqueVehiculeService: MarqueVehiculeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MarqueVehiculeUpdateComponent],
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
      .overrideTemplate(MarqueVehiculeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MarqueVehiculeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    marqueVehiculeService = TestBed.inject(MarqueVehiculeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const marqueVehicule: IMarqueVehicule = { id: 456 };

      activatedRoute.data = of({ marqueVehicule });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(marqueVehicule));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MarqueVehicule>>();
      const marqueVehicule = { id: 123 };
      jest.spyOn(marqueVehiculeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ marqueVehicule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: marqueVehicule }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(marqueVehiculeService.update).toHaveBeenCalledWith(marqueVehicule);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MarqueVehicule>>();
      const marqueVehicule = new MarqueVehicule();
      jest.spyOn(marqueVehiculeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ marqueVehicule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: marqueVehicule }));
      saveSubject.complete();

      // THEN
      expect(marqueVehiculeService.create).toHaveBeenCalledWith(marqueVehicule);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MarqueVehicule>>();
      const marqueVehicule = { id: 123 };
      jest.spyOn(marqueVehiculeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ marqueVehicule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(marqueVehiculeService.update).toHaveBeenCalledWith(marqueVehicule);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
