import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CarnetVehiculeService } from '../service/carnet-vehicule.service';

import { CarnetVehiculeComponent } from './carnet-vehicule.component';

describe('CarnetVehicule Management Component', () => {
  let comp: CarnetVehiculeComponent;
  let fixture: ComponentFixture<CarnetVehiculeComponent>;
  let service: CarnetVehiculeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CarnetVehiculeComponent],
    })
      .overrideTemplate(CarnetVehiculeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CarnetVehiculeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CarnetVehiculeService);

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
    expect(comp.carnetVehicules?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
