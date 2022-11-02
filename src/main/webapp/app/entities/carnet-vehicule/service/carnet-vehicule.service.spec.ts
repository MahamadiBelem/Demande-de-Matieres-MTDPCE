import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Etatvehicule } from 'app/entities/enumerations/etatvehicule.model';
import { ICarnetVehicule, CarnetVehicule } from '../carnet-vehicule.model';

import { CarnetVehiculeService } from './carnet-vehicule.service';

describe('CarnetVehicule Service', () => {
  let service: CarnetVehiculeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICarnetVehicule;
  let expectedResult: ICarnetVehicule | ICarnetVehicule[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CarnetVehiculeService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      date: currentDate,
      immatriculationVehicule: 'AAAAAAA',
      identiteConducteur: 'AAAAAAA',
      nombreReparation: 0,
      dateDerniereRevision: currentDate,
      etatvehicule: Etatvehicule.Operationel,
      observations: 'AAAAAAA',
      statutSup: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          date: currentDate.format(DATE_TIME_FORMAT),
          dateDerniereRevision: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CarnetVehicule', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          date: currentDate.format(DATE_TIME_FORMAT),
          dateDerniereRevision: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
          dateDerniereRevision: currentDate,
        },
        returnedFromService
      );

      service.create(new CarnetVehicule()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CarnetVehicule', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
          immatriculationVehicule: 'BBBBBB',
          identiteConducteur: 'BBBBBB',
          nombreReparation: 1,
          dateDerniereRevision: currentDate.format(DATE_FORMAT),
          etatvehicule: 'BBBBBB',
          observations: 'BBBBBB',
          statutSup: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
          dateDerniereRevision: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CarnetVehicule', () => {
      const patchObject = Object.assign(
        {
          immatriculationVehicule: 'BBBBBB',
          identiteConducteur: 'BBBBBB',
          observations: 'BBBBBB',
          statutSup: true,
        },
        new CarnetVehicule()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          date: currentDate,
          dateDerniereRevision: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CarnetVehicule', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
          immatriculationVehicule: 'BBBBBB',
          identiteConducteur: 'BBBBBB',
          nombreReparation: 1,
          dateDerniereRevision: currentDate.format(DATE_FORMAT),
          etatvehicule: 'BBBBBB',
          observations: 'BBBBBB',
          statutSup: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
          dateDerniereRevision: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CarnetVehicule', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCarnetVehiculeToCollectionIfMissing', () => {
      it('should add a CarnetVehicule to an empty array', () => {
        const carnetVehicule: ICarnetVehicule = { id: 123 };
        expectedResult = service.addCarnetVehiculeToCollectionIfMissing([], carnetVehicule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(carnetVehicule);
      });

      it('should not add a CarnetVehicule to an array that contains it', () => {
        const carnetVehicule: ICarnetVehicule = { id: 123 };
        const carnetVehiculeCollection: ICarnetVehicule[] = [
          {
            ...carnetVehicule,
          },
          { id: 456 },
        ];
        expectedResult = service.addCarnetVehiculeToCollectionIfMissing(carnetVehiculeCollection, carnetVehicule);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CarnetVehicule to an array that doesn't contain it", () => {
        const carnetVehicule: ICarnetVehicule = { id: 123 };
        const carnetVehiculeCollection: ICarnetVehicule[] = [{ id: 456 }];
        expectedResult = service.addCarnetVehiculeToCollectionIfMissing(carnetVehiculeCollection, carnetVehicule);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(carnetVehicule);
      });

      it('should add only unique CarnetVehicule to an array', () => {
        const carnetVehiculeArray: ICarnetVehicule[] = [{ id: 123 }, { id: 456 }, { id: 9330 }];
        const carnetVehiculeCollection: ICarnetVehicule[] = [{ id: 123 }];
        expectedResult = service.addCarnetVehiculeToCollectionIfMissing(carnetVehiculeCollection, ...carnetVehiculeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const carnetVehicule: ICarnetVehicule = { id: 123 };
        const carnetVehicule2: ICarnetVehicule = { id: 456 };
        expectedResult = service.addCarnetVehiculeToCollectionIfMissing([], carnetVehicule, carnetVehicule2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(carnetVehicule);
        expect(expectedResult).toContain(carnetVehicule2);
      });

      it('should accept null and undefined values', () => {
        const carnetVehicule: ICarnetVehicule = { id: 123 };
        expectedResult = service.addCarnetVehiculeToCollectionIfMissing([], null, carnetVehicule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(carnetVehicule);
      });

      it('should return initial array if no CarnetVehicule is added', () => {
        const carnetVehiculeCollection: ICarnetVehicule[] = [{ id: 123 }];
        expectedResult = service.addCarnetVehiculeToCollectionIfMissing(carnetVehiculeCollection, undefined, null);
        expect(expectedResult).toEqual(carnetVehiculeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
