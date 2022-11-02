import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DemandeMatieresService } from '../service/demande-matieres.service';

import { DemandeMatieresComponent } from './demande-matieres.component';

describe('DemandeMatieres Management Component', () => {
  let comp: DemandeMatieresComponent;
  let fixture: ComponentFixture<DemandeMatieresComponent>;
  let service: DemandeMatieresService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DemandeMatieresComponent],
    })
      .overrideTemplate(DemandeMatieresComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DemandeMatieresComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DemandeMatieresService);

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
    expect(comp.demandeMatieres?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
