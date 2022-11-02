import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MatieresService } from '../service/matieres.service';

import { MatieresComponent } from './matieres.component';

describe('Matieres Management Component', () => {
  let comp: MatieresComponent;
  let fixture: ComponentFixture<MatieresComponent>;
  let service: MatieresService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MatieresComponent],
    })
      .overrideTemplate(MatieresComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MatieresComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MatieresService);

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
    expect(comp.matieres?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
