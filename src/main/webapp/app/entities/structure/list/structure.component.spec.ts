import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { StructureService } from '../service/structure.service';

import { StructureComponent } from './structure.component';

describe('Structure Management Component', () => {
  let comp: StructureComponent;
  let fixture: ComponentFixture<StructureComponent>;
  let service: StructureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [StructureComponent],
    })
      .overrideTemplate(StructureComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StructureComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(StructureService);

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
    expect(comp.structures?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
