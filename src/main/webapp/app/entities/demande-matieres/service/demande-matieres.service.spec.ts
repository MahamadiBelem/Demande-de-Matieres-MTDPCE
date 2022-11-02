import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDemandeMatieres, DemandeMatieres } from '../demande-matieres.model';

import { DemandeMatieresService } from './demande-matieres.service';

describe('DemandeMatieres Service', () => {
  let service: DemandeMatieresService;
  let httpMock: HttpTestingController;
  let elemDefault: IDemandeMatieres;
  let expectedResult: IDemandeMatieres | IDemandeMatieres[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DemandeMatieresService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      date: currentDate,
      indentiteSoumettant: 'AAAAAAA',
      fonction: 'AAAAAAA',
      designation: 'AAAAAAA',
      quantite: 0,
      observation: 'AAAAAAA',
      statutSup: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a DemandeMatieres', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.create(new DemandeMatieres()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DemandeMatieres', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
          indentiteSoumettant: 'BBBBBB',
          fonction: 'BBBBBB',
          designation: 'BBBBBB',
          quantite: 1,
          observation: 'BBBBBB',
          statutSup: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DemandeMatieres', () => {
      const patchObject = Object.assign(
        {
          date: currentDate.format(DATE_TIME_FORMAT),
          designation: 'BBBBBB',
          quantite: 1,
          observation: 'BBBBBB',
          statutSup: true,
        },
        new DemandeMatieres()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DemandeMatieres', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
          indentiteSoumettant: 'BBBBBB',
          fonction: 'BBBBBB',
          designation: 'BBBBBB',
          quantite: 1,
          observation: 'BBBBBB',
          statutSup: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a DemandeMatieres', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDemandeMatieresToCollectionIfMissing', () => {
      it('should add a DemandeMatieres to an empty array', () => {
        const demandeMatieres: IDemandeMatieres = { id: 123 };
        expectedResult = service.addDemandeMatieresToCollectionIfMissing([], demandeMatieres);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(demandeMatieres);
      });

      it('should not add a DemandeMatieres to an array that contains it', () => {
        const demandeMatieres: IDemandeMatieres = { id: 123 };
        const demandeMatieresCollection: IDemandeMatieres[] = [
          {
            ...demandeMatieres,
          },
          { id: 456 },
        ];
        expectedResult = service.addDemandeMatieresToCollectionIfMissing(demandeMatieresCollection, demandeMatieres);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DemandeMatieres to an array that doesn't contain it", () => {
        const demandeMatieres: IDemandeMatieres = { id: 123 };
        const demandeMatieresCollection: IDemandeMatieres[] = [{ id: 456 }];
        expectedResult = service.addDemandeMatieresToCollectionIfMissing(demandeMatieresCollection, demandeMatieres);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(demandeMatieres);
      });

      it('should add only unique DemandeMatieres to an array', () => {
        const demandeMatieresArray: IDemandeMatieres[] = [{ id: 123 }, { id: 456 }, { id: 13331 }];
        const demandeMatieresCollection: IDemandeMatieres[] = [{ id: 123 }];
        expectedResult = service.addDemandeMatieresToCollectionIfMissing(demandeMatieresCollection, ...demandeMatieresArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const demandeMatieres: IDemandeMatieres = { id: 123 };
        const demandeMatieres2: IDemandeMatieres = { id: 456 };
        expectedResult = service.addDemandeMatieresToCollectionIfMissing([], demandeMatieres, demandeMatieres2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(demandeMatieres);
        expect(expectedResult).toContain(demandeMatieres2);
      });

      it('should accept null and undefined values', () => {
        const demandeMatieres: IDemandeMatieres = { id: 123 };
        expectedResult = service.addDemandeMatieresToCollectionIfMissing([], null, demandeMatieres, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(demandeMatieres);
      });

      it('should return initial array if no DemandeMatieres is added', () => {
        const demandeMatieresCollection: IDemandeMatieres[] = [{ id: 123 }];
        expectedResult = service.addDemandeMatieresToCollectionIfMissing(demandeMatieresCollection, undefined, null);
        expect(expectedResult).toEqual(demandeMatieresCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
