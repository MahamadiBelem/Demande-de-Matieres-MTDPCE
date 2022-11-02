import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRevisionVehicule, RevisionVehicule } from '../revision-vehicule.model';

import { RevisionVehiculeService } from './revision-vehicule.service';

describe('RevisionVehicule Service', () => {
  let service: RevisionVehiculeService;
  let httpMock: HttpTestingController;
  let elemDefault: IRevisionVehicule;
  let expectedResult: IRevisionVehicule | IRevisionVehicule[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RevisionVehiculeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      libelleRevisionVehicule: 'AAAAAAA',
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

    it('should create a RevisionVehicule', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new RevisionVehicule()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RevisionVehicule', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelleRevisionVehicule: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RevisionVehicule', () => {
      const patchObject = Object.assign(
        {
          libelleRevisionVehicule: 'BBBBBB',
        },
        new RevisionVehicule()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RevisionVehicule', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelleRevisionVehicule: 'BBBBBB',
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

    it('should delete a RevisionVehicule', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRevisionVehiculeToCollectionIfMissing', () => {
      it('should add a RevisionVehicule to an empty array', () => {
        const revisionVehicule: IRevisionVehicule = { id: 123 };
        expectedResult = service.addRevisionVehiculeToCollectionIfMissing([], revisionVehicule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(revisionVehicule);
      });

      it('should not add a RevisionVehicule to an array that contains it', () => {
        const revisionVehicule: IRevisionVehicule = { id: 123 };
        const revisionVehiculeCollection: IRevisionVehicule[] = [
          {
            ...revisionVehicule,
          },
          { id: 456 },
        ];
        expectedResult = service.addRevisionVehiculeToCollectionIfMissing(revisionVehiculeCollection, revisionVehicule);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RevisionVehicule to an array that doesn't contain it", () => {
        const revisionVehicule: IRevisionVehicule = { id: 123 };
        const revisionVehiculeCollection: IRevisionVehicule[] = [{ id: 456 }];
        expectedResult = service.addRevisionVehiculeToCollectionIfMissing(revisionVehiculeCollection, revisionVehicule);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(revisionVehicule);
      });

      it('should add only unique RevisionVehicule to an array', () => {
        const revisionVehiculeArray: IRevisionVehicule[] = [{ id: 123 }, { id: 456 }, { id: 80396 }];
        const revisionVehiculeCollection: IRevisionVehicule[] = [{ id: 123 }];
        expectedResult = service.addRevisionVehiculeToCollectionIfMissing(revisionVehiculeCollection, ...revisionVehiculeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const revisionVehicule: IRevisionVehicule = { id: 123 };
        const revisionVehicule2: IRevisionVehicule = { id: 456 };
        expectedResult = service.addRevisionVehiculeToCollectionIfMissing([], revisionVehicule, revisionVehicule2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(revisionVehicule);
        expect(expectedResult).toContain(revisionVehicule2);
      });

      it('should accept null and undefined values', () => {
        const revisionVehicule: IRevisionVehicule = { id: 123 };
        expectedResult = service.addRevisionVehiculeToCollectionIfMissing([], null, revisionVehicule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(revisionVehicule);
      });

      it('should return initial array if no RevisionVehicule is added', () => {
        const revisionVehiculeCollection: IRevisionVehicule[] = [{ id: 123 }];
        expectedResult = service.addRevisionVehiculeToCollectionIfMissing(revisionVehiculeCollection, undefined, null);
        expect(expectedResult).toEqual(revisionVehiculeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
