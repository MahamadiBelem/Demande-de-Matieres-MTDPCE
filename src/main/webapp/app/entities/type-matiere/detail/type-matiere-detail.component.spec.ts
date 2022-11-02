import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TypeMatiereDetailComponent } from './type-matiere-detail.component';

describe('TypeMatiere Management Detail Component', () => {
  let comp: TypeMatiereDetailComponent;
  let fixture: ComponentFixture<TypeMatiereDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TypeMatiereDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ typeMatiere: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TypeMatiereDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TypeMatiereDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load typeMatiere on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.typeMatiere).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
