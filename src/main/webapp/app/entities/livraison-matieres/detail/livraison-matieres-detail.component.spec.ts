import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LivraisonMatieresDetailComponent } from './livraison-matieres-detail.component';

describe('LivraisonMatieres Management Detail Component', () => {
  let comp: LivraisonMatieresDetailComponent;
  let fixture: ComponentFixture<LivraisonMatieresDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LivraisonMatieresDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ livraisonMatieres: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LivraisonMatieresDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LivraisonMatieresDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load livraisonMatieres on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.livraisonMatieres).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
