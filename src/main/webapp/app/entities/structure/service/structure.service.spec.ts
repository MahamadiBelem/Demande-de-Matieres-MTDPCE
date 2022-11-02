import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IStructure, Structure } from '../structure.model';

import { StructureService } from './structure.service';

describe('Structure Service', () => {
  let service: StructureService;
  let httpMock: HttpTestingController;
  let elemDefault: IStructure;
  let expectedResult: IStructure | IStructure[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StructureService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      libelleStructure: 'AAAAAAA',
      codeStructure: 'AAAAAAA',
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

    it('should create a Structure', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Structure()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Structure', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelleStructure: 'BBBBBB',
          codeStructure: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Structure', () => {
      const patchObject = Object.assign({}, new Structure());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Structure', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelleStructure: 'BBBBBB',
          codeStructure: 'BBBBBB',
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

    it('should delete a Structure', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addStructureToCollectionIfMissing', () => {
      it('should add a Structure to an empty array', () => {
        const structure: IStructure = { id: 123 };
        expectedResult = service.addStructureToCollectionIfMissing([], structure);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(structure);
      });

      it('should not add a Structure to an array that contains it', () => {
        const structure: IStructure = { id: 123 };
        const structureCollection: IStructure[] = [
          {
            ...structure,
          },
          { id: 456 },
        ];
        expectedResult = service.addStructureToCollectionIfMissing(structureCollection, structure);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Structure to an array that doesn't contain it", () => {
        const structure: IStructure = { id: 123 };
        const structureCollection: IStructure[] = [{ id: 456 }];
        expectedResult = service.addStructureToCollectionIfMissing(structureCollection, structure);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(structure);
      });

      it('should add only unique Structure to an array', () => {
        const structureArray: IStructure[] = [{ id: 123 }, { id: 456 }, { id: 98923 }];
        const structureCollection: IStructure[] = [{ id: 123 }];
        expectedResult = service.addStructureToCollectionIfMissing(structureCollection, ...structureArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const structure: IStructure = { id: 123 };
        const structure2: IStructure = { id: 456 };
        expectedResult = service.addStructureToCollectionIfMissing([], structure, structure2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(structure);
        expect(expectedResult).toContain(structure2);
      });

      it('should accept null and undefined values', () => {
        const structure: IStructure = { id: 123 };
        expectedResult = service.addStructureToCollectionIfMissing([], null, structure, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(structure);
      });

      it('should return initial array if no Structure is added', () => {
        const structureCollection: IStructure[] = [{ id: 123 }];
        expectedResult = service.addStructureToCollectionIfMissing(structureCollection, undefined, null);
        expect(expectedResult).toEqual(structureCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
