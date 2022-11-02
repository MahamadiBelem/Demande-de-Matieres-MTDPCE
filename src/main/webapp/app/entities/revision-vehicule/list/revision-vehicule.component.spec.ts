import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RevisionVehiculeService } from '../service/revision-vehicule.service';

import { RevisionVehiculeComponent } from './revision-vehicule.component';

describe('RevisionVehicule Management Component', () => {
  let comp: RevisionVehiculeComponent;
  let fixture: ComponentFixture<RevisionVehiculeComponent>;
  let service: RevisionVehiculeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RevisionVehiculeComponent],
    })
      .overrideTemplate(RevisionVehiculeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RevisionVehiculeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RevisionVehiculeService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.revisionVehicules?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
