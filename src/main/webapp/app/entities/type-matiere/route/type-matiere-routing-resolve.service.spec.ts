import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITypeMatiere, TypeMatiere } from '../type-matiere.model';
import { TypeMatiereService } from '../service/type-matiere.service';

import { TypeMatiereRoutingResolveService } from './type-matiere-routing-resolve.service';

describe('TypeMatiere routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TypeMatiereRoutingResolveService;
  let service: TypeMatiereService;
  let resultTypeMatiere: ITypeMatiere | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(TypeMatiereRoutingResolveService);
    service = TestBed.inject(TypeMatiereService);
    resultTypeMatiere = undefined;
  });

  describe('resolve', () => {
    it('should return ITypeMatiere returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTypeMatiere = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTypeMatiere).toEqual({ id: 123 });
    });

    it('should return new ITypeMatiere if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTypeMatiere = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTypeMatiere).toEqual(new TypeMatiere());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TypeMatiere })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTypeMatiere = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTypeMatiere).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
