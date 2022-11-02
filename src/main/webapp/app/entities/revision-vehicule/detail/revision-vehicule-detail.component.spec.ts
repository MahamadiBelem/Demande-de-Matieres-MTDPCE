import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RevisionVehiculeDetailComponent } from './revision-vehicule-detail.component';

describe('RevisionVehicule Management Detail Component', () => {
  let comp: RevisionVehiculeDetailComponent;
  let fixture: ComponentFixture<RevisionVehiculeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RevisionVehiculeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ revisionVehicule: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RevisionVehiculeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RevisionVehiculeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load revisionVehicule on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.revisionVehicule).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
