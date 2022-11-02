import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TypeMatiereService } from '../service/type-matiere.service';

import { TypeMatiereComponent } from './type-matiere.component';

describe('TypeMatiere Management Component', () => {
  let comp: TypeMatiereComponent;
  let fixture: ComponentFixture<TypeMatiereComponent>;
  let service: TypeMatiereService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TypeMatiereComponent],
    })
      .overrideTemplate(TypeMatiereComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeMatiereComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TypeMatiereService);

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
    expect(comp.typeMatieres?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
