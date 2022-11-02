import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DemandeReparationsService } from '../service/demande-reparations.service';

import { DemandeReparationsComponent } from './demande-reparations.component';

describe('DemandeReparations Management Component', () => {
  let comp: DemandeReparationsComponent;
  let fixture: ComponentFixture<DemandeReparationsComponent>;
  let service: DemandeReparationsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DemandeReparationsComponent],
    })
      .overrideTemplate(DemandeReparationsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DemandeReparationsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DemandeReparationsService);

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
    expect(comp.demandeReparations?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
