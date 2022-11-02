import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeMatiere, TypeMatiere } from '../type-matiere.model';

import { TypeMatiereService } from './type-matiere.service';

describe('TypeMatiere Service', () => {
  let service: TypeMatiereService;
  let httpMock: HttpTestingController;
  let elemDefault: ITypeMatiere;
  let expectedResult: ITypeMatiere | ITypeMatiere[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypeMatiereService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      libelleTypeMatiere: 'AAAAAAA',
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

    it('should create a TypeMatiere', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TypeMatiere()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeMatiere', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelleTypeMatiere: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeMatiere', () => {
      const patchObject = Object.assign(
        {
          libelleTypeMatiere: 'BBBBBB',
        },
        new TypeMatiere()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeMatiere', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelleTypeMatiere: 'BBBBBB',
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

    it('should delete a TypeMatiere', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTypeMatiereToCollectionIfMissing', () => {
      it('should add a TypeMatiere to an empty array', () => {
        const typeMatiere: ITypeMatiere = { id: 123 };
        expectedResult = service.addTypeMatiereToCollectionIfMissing([], typeMatiere);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeMatiere);
      });

      it('should not add a TypeMatiere to an array that contains it', () => {
        const typeMatiere: ITypeMatiere = { id: 123 };
        const typeMatiereCollection: ITypeMatiere[] = [
          {
            ...typeMatiere,
          },
          { id: 456 },
        ];
        expectedResult = service.addTypeMatiereToCollectionIfMissing(typeMatiereCollection, typeMatiere);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeMatiere to an array that doesn't contain it", () => {
        const typeMatiere: ITypeMatiere = { id: 123 };
        const typeMatiereCollection: ITypeMatiere[] = [{ id: 456 }];
        expectedResult = service.addTypeMatiereToCollectionIfMissing(typeMatiereCollection, typeMatiere);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeMatiere);
      });

      it('should add only unique TypeMatiere to an array', () => {
        const typeMatiereArray: ITypeMatiere[] = [{ id: 123 }, { id: 456 }, { id: 76703 }];
        const typeMatiereCollection: ITypeMatiere[] = [{ id: 123 }];
        expectedResult = service.addTypeMatiereToCollectionIfMissing(typeMatiereCollection, ...typeMatiereArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeMatiere: ITypeMatiere = { id: 123 };
        const typeMatiere2: ITypeMatiere = { id: 456 };
        expectedResult = service.addTypeMatiereToCollectionIfMissing([], typeMatiere, typeMatiere2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeMatiere);
        expect(expectedResult).toContain(typeMatiere2);
      });

      it('should accept null and undefined values', () => {
        const typeMatiere: ITypeMatiere = { id: 123 };
        expectedResult = service.addTypeMatiereToCollectionIfMissing([], null, typeMatiere, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeMatiere);
      });

      it('should return initial array if no TypeMatiere is added', () => {
        const typeMatiereCollection: ITypeMatiere[] = [{ id: 123 }];
        expectedResult = service.addTypeMatiereToCollectionIfMissing(typeMatiereCollection, undefined, null);
        expect(expectedResult).toEqual(typeMatiereCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
