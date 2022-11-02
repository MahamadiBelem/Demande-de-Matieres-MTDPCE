import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TypePanneDetailComponent } from './type-panne-detail.component';

describe('TypePanne Management Detail Component', () => {
  let comp: TypePanneDetailComponent;
  let fixture: ComponentFixture<TypePanneDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TypePanneDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ typePanne: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TypePanneDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TypePanneDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load typePanne on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.typePanne).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
