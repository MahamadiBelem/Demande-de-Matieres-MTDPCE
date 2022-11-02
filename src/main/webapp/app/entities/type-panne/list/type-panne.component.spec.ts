import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TypePanneService } from '../service/type-panne.service';

import { TypePanneComponent } from './type-panne.component';

describe('TypePanne Management Component', () => {
  let comp: TypePanneComponent;
  let fixture: ComponentFixture<TypePanneComponent>;
  let service: TypePanneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TypePanneComponent],
    })
      .overrideTemplate(TypePanneComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypePanneComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TypePanneService);

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
    expect(comp.typePannes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
