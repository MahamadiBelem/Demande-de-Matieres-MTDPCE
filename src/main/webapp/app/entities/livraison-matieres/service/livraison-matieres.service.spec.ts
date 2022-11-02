import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILivraisonMatieres, LivraisonMatieres } from '../livraison-matieres.model';

import { LivraisonMatieresService } from './livraison-matieres.service';

describe('LivraisonMatieres Service', () => {
  let service: LivraisonMatieresService;
  let httpMock: HttpTestingController;
  let elemDefault: ILivraisonMatieres;
  let expectedResult: ILivraisonMatieres | ILivraisonMatieres[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LivraisonMatieresService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      designationMatiere: 'AAAAAAA',
      quantiteLivree: 0,
      dateLivree: currentDate,
      statutSup: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateLivree: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a LivraisonMatieres', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateLivree: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateLivree: currentDate,
        },
        returnedFromService
      );

      service.create(new LivraisonMatieres()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LivraisonMatieres', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          designationMatiere: 'BBBBBB',
          quantiteLivree: 1,
          dateLivree: currentDate.format(DATE_TIME_FORMAT),
          statutSup: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateLivree: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LivraisonMatieres', () => {
      const patchObject = Object.assign(
        {
          quantiteLivree: 1,
          dateLivree: currentDate.format(DATE_TIME_FORMAT),
          statutSup: true,
        },
        new LivraisonMatieres()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateLivree: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LivraisonMatieres', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          designationMatiere: 'BBBBBB',
          quantiteLivree: 1,
          dateLivree: currentDate.format(DATE_TIME_FORMAT),
          statutSup: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateLivree: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a LivraisonMatieres', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLivraisonMatieresToCollectionIfMissing', () => {
      it('should add a LivraisonMatieres to an empty array', () => {
        const livraisonMatieres: ILivraisonMatieres = { id: 123 };
        expectedResult = service.addLivraisonMatieresToCollectionIfMissing([], livraisonMatieres);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(livraisonMatieres);
      });

      it('should not add a LivraisonMatieres to an array that contains it', () => {
        const livraisonMatieres: ILivraisonMatieres = { id: 123 };
        const livraisonMatieresCollection: ILivraisonMatieres[] = [
          {
            ...livraisonMatieres,
          },
          { id: 456 },
        ];
        expectedResult = service.addLivraisonMatieresToCollectionIfMissing(livraisonMatieresCollection, livraisonMatieres);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LivraisonMatieres to an array that doesn't contain it", () => {
        const livraisonMatieres: ILivraisonMatieres = { id: 123 };
        const livraisonMatieresCollection: ILivraisonMatieres[] = [{ id: 456 }];
        expectedResult = service.addLivraisonMatieresToCollectionIfMissing(livraisonMatieresCollection, livraisonMatieres);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(livraisonMatieres);
      });

      it('should add only unique LivraisonMatieres to an array', () => {
        const livraisonMatieresArray: ILivraisonMatieres[] = [{ id: 123 }, { id: 456 }, { id: 63053 }];
        const livraisonMatieresCollection: ILivraisonMatieres[] = [{ id: 123 }];
        expectedResult = service.addLivraisonMatieresToCollectionIfMissing(livraisonMatieresCollection, ...livraisonMatieresArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const livraisonMatieres: ILivraisonMatieres = { id: 123 };
        const livraisonMatieres2: ILivraisonMatieres = { id: 456 };
        expectedResult = service.addLivraisonMatieresToCollectionIfMissing([], livraisonMatieres, livraisonMatieres2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(livraisonMatieres);
        expect(expectedResult).toContain(livraisonMatieres2);
      });

      it('should accept null and undefined values', () => {
        const livraisonMatieres: ILivraisonMatieres = { id: 123 };
        expectedResult = service.addLivraisonMatieresToCollectionIfMissing([], null, livraisonMatieres, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(livraisonMatieres);
      });

      it('should return initial array if no LivraisonMatieres is added', () => {
        const livraisonMatieresCollection: ILivraisonMatieres[] = [{ id: 123 }];
        expectedResult = service.addLivraisonMatieresToCollectionIfMissing(livraisonMatieresCollection, undefined, null);
        expect(expectedResult).toEqual(livraisonMatieresCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
