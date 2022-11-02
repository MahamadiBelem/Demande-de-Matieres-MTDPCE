import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMarqueVehicule, MarqueVehicule } from '../marque-vehicule.model';

import { MarqueVehiculeService } from './marque-vehicule.service';

describe('MarqueVehicule Service', () => {
  let service: MarqueVehiculeService;
  let httpMock: HttpTestingController;
  let elemDefault: IMarqueVehicule;
  let expectedResult: IMarqueVehicule | IMarqueVehicule[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MarqueVehiculeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      libelleMarqueVehicule: 'AAAAAAA',
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

    it('should create a MarqueVehicule', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new MarqueVehicule()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MarqueVehicule', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelleMarqueVehicule: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MarqueVehicule', () => {
      const patchObject = Object.assign({}, new MarqueVehicule());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MarqueVehicule', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelleMarqueVehicule: 'BBBBBB',
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

    it('should delete a MarqueVehicule', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMarqueVehiculeToCollectionIfMissing', () => {
      it('should add a MarqueVehicule to an empty array', () => {
        const marqueVehicule: IMarqueVehicule = { id: 123 };
        expectedResult = service.addMarqueVehiculeToCollectionIfMissing([], marqueVehicule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(marqueVehicule);
      });

      it('should not add a MarqueVehicule to an array that contains it', () => {
        const marqueVehicule: IMarqueVehicule = { id: 123 };
        const marqueVehiculeCollection: IMarqueVehicule[] = [
          {
            ...marqueVehicule,
          },
          { id: 456 },
        ];
        expectedResult = service.addMarqueVehiculeToCollectionIfMissing(marqueVehiculeCollection, marqueVehicule);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MarqueVehicule to an array that doesn't contain it", () => {
        const marqueVehicule: IMarqueVehicule = { id: 123 };
        const marqueVehiculeCollection: IMarqueVehicule[] = [{ id: 456 }];
        expectedResult = service.addMarqueVehiculeToCollectionIfMissing(marqueVehiculeCollection, marqueVehicule);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(marqueVehicule);
      });

      it('should add only unique MarqueVehicule to an array', () => {
        const marqueVehiculeArray: IMarqueVehicule[] = [{ id: 123 }, { id: 456 }, { id: 36439 }];
        const marqueVehiculeCollection: IMarqueVehicule[] = [{ id: 123 }];
        expectedResult = service.addMarqueVehiculeToCollectionIfMissing(marqueVehiculeCollection, ...marqueVehiculeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const marqueVehicule: IMarqueVehicule = { id: 123 };
        const marqueVehicule2: IMarqueVehicule = { id: 456 };
        expectedResult = service.addMarqueVehiculeToCollectionIfMissing([], marqueVehicule, marqueVehicule2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(marqueVehicule);
        expect(expectedResult).toContain(marqueVehicule2);
      });

      it('should accept null and undefined values', () => {
        const marqueVehicule: IMarqueVehicule = { id: 123 };
        expectedResult = service.addMarqueVehiculeToCollectionIfMissing([], null, marqueVehicule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(marqueVehicule);
      });

      it('should return initial array if no MarqueVehicule is added', () => {
        const marqueVehiculeCollection: IMarqueVehicule[] = [{ id: 123 }];
        expectedResult = service.addMarqueVehiculeToCollectionIfMissing(marqueVehiculeCollection, undefined, null);
        expect(expectedResult).toEqual(marqueVehiculeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
