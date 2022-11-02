import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDemandeReparations, DemandeReparations } from '../demande-reparations.model';

import { DemandeReparationsService } from './demande-reparations.service';

describe('DemandeReparations Service', () => {
  let service: DemandeReparationsService;
  let httpMock: HttpTestingController;
  let elemDefault: IDemandeReparations;
  let expectedResult: IDemandeReparations | IDemandeReparations[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DemandeReparationsService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      date: currentDate,
      indentiteSoumettant: 'AAAAAAA',
      fonction: 'AAAAAAA',
      designation: 'AAAAAAA',
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

    it('should create a DemandeReparations', () => {
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

      service.create(new DemandeReparations()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DemandeReparations', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
          indentiteSoumettant: 'BBBBBB',
          fonction: 'BBBBBB',
          designation: 'BBBBBB',
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

    it('should partial update a DemandeReparations', () => {
      const patchObject = Object.assign(
        {
          designation: 'BBBBBB',
          observation: 'BBBBBB',
          statutSup: true,
        },
        new DemandeReparations()
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

    it('should return a list of DemandeReparations', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
          indentiteSoumettant: 'BBBBBB',
          fonction: 'BBBBBB',
          designation: 'BBBBBB',
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

    it('should delete a DemandeReparations', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDemandeReparationsToCollectionIfMissing', () => {
      it('should add a DemandeReparations to an empty array', () => {
        const demandeReparations: IDemandeReparations = { id: 123 };
        expectedResult = service.addDemandeReparationsToCollectionIfMissing([], demandeReparations);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(demandeReparations);
      });

      it('should not add a DemandeReparations to an array that contains it', () => {
        const demandeReparations: IDemandeReparations = { id: 123 };
        const demandeReparationsCollection: IDemandeReparations[] = [
          {
            ...demandeReparations,
          },
          { id: 456 },
        ];
        expectedResult = service.addDemandeReparationsToCollectionIfMissing(demandeReparationsCollection, demandeReparations);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DemandeReparations to an array that doesn't contain it", () => {
        const demandeReparations: IDemandeReparations = { id: 123 };
        const demandeReparationsCollection: IDemandeReparations[] = [{ id: 456 }];
        expectedResult = service.addDemandeReparationsToCollectionIfMissing(demandeReparationsCollection, demandeReparations);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(demandeReparations);
      });

      it('should add only unique DemandeReparations to an array', () => {
        const demandeReparationsArray: IDemandeReparations[] = [{ id: 123 }, { id: 456 }, { id: 21957 }];
        const demandeReparationsCollection: IDemandeReparations[] = [{ id: 123 }];
        expectedResult = service.addDemandeReparationsToCollectionIfMissing(demandeReparationsCollection, ...demandeReparationsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const demandeReparations: IDemandeReparations = { id: 123 };
        const demandeReparations2: IDemandeReparations = { id: 456 };
        expectedResult = service.addDemandeReparationsToCollectionIfMissing([], demandeReparations, demandeReparations2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(demandeReparations);
        expect(expectedResult).toContain(demandeReparations2);
      });

      it('should accept null and undefined values', () => {
        const demandeReparations: IDemandeReparations = { id: 123 };
        expectedResult = service.addDemandeReparationsToCollectionIfMissing([], null, demandeReparations, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(demandeReparations);
      });

      it('should return initial array if no DemandeReparations is added', () => {
        const demandeReparationsCollection: IDemandeReparations[] = [{ id: 123 }];
        expectedResult = service.addDemandeReparationsToCollectionIfMissing(demandeReparationsCollection, undefined, null);
        expect(expectedResult).toEqual(demandeReparationsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
