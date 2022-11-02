import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MatieresService } from '../service/matieres.service';
import { IMatieres, Matieres } from '../matieres.model';

import { MatieresUpdateComponent } from './matieres-update.component';

describe('Matieres Management Update Component', () => {
  let comp: MatieresUpdateComponent;
  let fixture: ComponentFixture<MatieresUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let matieresService: MatieresService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MatieresUpdateComponent],
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
      .overrideTemplate(MatieresUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MatieresUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    matieresService = TestBed.inject(MatieresService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const matieres: IMatieres = { id: 456 };

      activatedRoute.data = of({ matieres });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(matieres));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Matieres>>();
      const matieres = { id: 123 };
      jest.spyOn(matieresService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matieres });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: matieres }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(matieresService.update).toHaveBeenCalledWith(matieres);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Matieres>>();
      const matieres = new Matieres();
      jest.spyOn(matieresService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matieres });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: matieres }));
      saveSubject.complete();

      // THEN
      expect(matieresService.create).toHaveBeenCalledWith(matieres);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Matieres>>();
      const matieres = { id: 123 };
      jest.spyOn(matieresService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matieres });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(matieresService.update).toHaveBeenCalledWith(matieres);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
