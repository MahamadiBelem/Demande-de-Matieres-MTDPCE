import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypeMatiereService } from '../service/type-matiere.service';
import { ITypeMatiere, TypeMatiere } from '../type-matiere.model';

import { TypeMatiereUpdateComponent } from './type-matiere-update.component';

describe('TypeMatiere Management Update Component', () => {
  let comp: TypeMatiereUpdateComponent;
  let fixture: ComponentFixture<TypeMatiereUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeMatiereService: TypeMatiereService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TypeMatiereUpdateComponent],
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
      .overrideTemplate(TypeMatiereUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeMatiereUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeMatiereService = TestBed.inject(TypeMatiereService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typeMatiere: ITypeMatiere = { id: 456 };

      activatedRoute.data = of({ typeMatiere });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(typeMatiere));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypeMatiere>>();
      const typeMatiere = { id: 123 };
      jest.spyOn(typeMatiereService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeMatiere });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeMatiere }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeMatiereService.update).toHaveBeenCalledWith(typeMatiere);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypeMatiere>>();
      const typeMatiere = new TypeMatiere();
      jest.spyOn(typeMatiereService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeMatiere });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeMatiere }));
      saveSubject.complete();

      // THEN
      expect(typeMatiereService.create).toHaveBeenCalledWith(typeMatiere);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypeMatiere>>();
      const typeMatiere = { id: 123 };
      jest.spyOn(typeMatiereService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeMatiere });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeMatiereService.update).toHaveBeenCalledWith(typeMatiere);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
