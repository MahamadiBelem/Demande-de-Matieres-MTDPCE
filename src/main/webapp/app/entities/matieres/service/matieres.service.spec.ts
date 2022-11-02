import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMatieres, Matieres } from '../matieres.model';

import { MatieresService } from './matieres.service';

describe('Matieres Service', () => {
  let service: MatieresService;
  let httpMock: HttpTestingController;
  let elemDefault: IMatieres;
  let expectedResult: IMatieres | IMatieres[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MatieresService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      designationMatieres: 'AAAAAAA',
      quantiteMatieres: 0,
      caracteristiquesMatieres: 'AAAAAAA',
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

    it('should create a Matieres', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Matieres()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Matieres', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          designationMatieres: 'BBBBBB',
          quantiteMatieres: 1,
          caracteristiquesMatieres: 'BBBBBB',
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

    it('should partial update a Matieres', () => {
      const patchObject = Object.assign(
        {
          designationMatieres: 'BBBBBB',
          statutSup: true,
        },
        new Matieres()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Matieres', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          designationMatieres: 'BBBBBB',
          quantiteMatieres: 1,
          caracteristiquesMatieres: 'BBBBBB',
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

    it('should delete a Matieres', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMatieresToCollectionIfMissing', () => {
      it('should add a Matieres to an empty array', () => {
        const matieres: IMatieres = { id: 123 };
        expectedResult = service.addMatieresToCollectionIfMissing([], matieres);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(matieres);
      });

      it('should not add a Matieres to an array that contains it', () => {
        const matieres: IMatieres = { id: 123 };
        const matieresCollection: IMatieres[] = [
          {
            ...matieres,
          },
          { id: 456 },
        ];
        expectedResult = service.addMatieresToCollectionIfMissing(matieresCollection, matieres);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Matieres to an array that doesn't contain it", () => {
        const matieres: IMatieres = { id: 123 };
        const matieresCollection: IMatieres[] = [{ id: 456 }];
        expectedResult = service.addMatieresToCollectionIfMissing(matieresCollection, matieres);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(matieres);
      });

      it('should add only unique Matieres to an array', () => {
        const matieresArray: IMatieres[] = [{ id: 123 }, { id: 456 }, { id: 68071 }];
        const matieresCollection: IMatieres[] = [{ id: 123 }];
        expectedResult = service.addMatieresToCollectionIfMissing(matieresCollection, ...matieresArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const matieres: IMatieres = { id: 123 };
        const matieres2: IMatieres = { id: 456 };
        expectedResult = service.addMatieresToCollectionIfMissing([], matieres, matieres2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(matieres);
        expect(expectedResult).toContain(matieres2);
      });

      it('should accept null and undefined values', () => {
        const matieres: IMatieres = { id: 123 };
        expectedResult = service.addMatieresToCollectionIfMissing([], null, matieres, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(matieres);
      });

      it('should return initial array if no Matieres is added', () => {
        const matieresCollection: IMatieres[] = [{ id: 123 }];
        expectedResult = service.addMatieresToCollectionIfMissing(matieresCollection, undefined, null);
        expect(expectedResult).toEqual(matieresCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
