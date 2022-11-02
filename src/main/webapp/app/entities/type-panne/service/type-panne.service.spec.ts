import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypePanne, TypePanne } from '../type-panne.model';

import { TypePanneService } from './type-panne.service';

describe('TypePanne Service', () => {
  let service: TypePanneService;
  let httpMock: HttpTestingController;
  let elemDefault: ITypePanne;
  let expectedResult: ITypePanne | ITypePanne[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypePanneService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      libelleTypePanne: 'AAAAAAA',
      statutSup: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a TypePanne', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TypePanne()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypePanne', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelleTypePanne: 'BBBBBB',
          statutSup: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypePanne', () => {
      const patchObject = Object.assign(
        {
          statutSup: true,
        },
        new TypePanne()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypePanne', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelleTypePanne: 'BBBBBB',
          statutSup: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a TypePanne', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTypePanneToCollectionIfMissing', () => {
      it('should add a TypePanne to an empty array', () => {
        const typePanne: ITypePanne = { id: 123 };
        expectedResult = service.addTypePanneToCollectionIfMissing([], typePanne);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typePanne);
      });

      it('should not add a TypePanne to an array that contains it', () => {
        const typePanne: ITypePanne = { id: 123 };
        const typePanneCollection: ITypePanne[] = [
          {
            ...typePanne,
          },
          { id: 456 },
        ];
        expectedResult = service.addTypePanneToCollectionIfMissing(typePanneCollection, typePanne);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypePanne to an array that doesn't contain it", () => {
        const typePanne: ITypePanne = { id: 123 };
        const typePanneCollection: ITypePanne[] = [{ id: 456 }];
        expectedResult = service.addTypePanneToCollectionIfMissing(typePanneCollection, typePanne);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typePanne);
      });

      it('should add only unique TypePanne to an array', () => {
        const typePanneArray: ITypePanne[] = [{ id: 123 }, { id: 456 }, { id: 31899 }];
        const typePanneCollection: ITypePanne[] = [{ id: 123 }];
        expectedResult = service.addTypePanneToCollectionIfMissing(typePanneCollection, ...typePanneArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typePanne: ITypePanne = { id: 123 };
        const typePanne2: ITypePanne = { id: 456 };
        expectedResult = service.addTypePanneToCollectionIfMissing([], typePanne, typePanne2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typePanne);
        expect(expectedResult).toContain(typePanne2);
      });

      it('should accept null and undefined values', () => {
        const typePanne: ITypePanne = { id: 123 };
        expectedResult = service.addTypePanneToCollectionIfMissing([], null, typePanne, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typePanne);
      });

      it('should return initial array if no TypePanne is added', () => {
        const typePanneCollection: ITypePanne[] = [{ id: 123 }];
        expectedResult = service.addTypePanneToCollectionIfMissing(typePanneCollection, undefined, null);
        expect(expectedResult).toEqual(typePanneCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
