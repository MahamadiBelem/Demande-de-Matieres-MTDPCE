import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MarqueVehiculeDetailComponent } from './marque-vehicule-detail.component';

describe('MarqueVehicule Management Detail Component', () => {
  let comp: MarqueVehiculeDetailComponent;
  let fixture: ComponentFixture<MarqueVehiculeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MarqueVehiculeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ marqueVehicule: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MarqueVehiculeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MarqueVehiculeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load marqueVehicule on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.marqueVehicule).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
