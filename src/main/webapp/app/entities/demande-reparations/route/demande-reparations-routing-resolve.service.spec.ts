import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IDemandeReparations, DemandeReparations } from '../demande-reparations.model';
import { DemandeReparationsService } from '../service/demande-reparations.service';

import { DemandeReparationsRoutingResolveService } from './demande-reparations-routing-resolve.service';

describe('DemandeReparations routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DemandeReparationsRoutingResolveService;
  let service: DemandeReparationsService;
  let resultDemandeReparations: IDemandeReparations | undefined;

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
    routingResolveService = TestBed.inject(DemandeReparationsRoutingResolveService);
    service = TestBed.inject(DemandeReparationsService);
    resultDemandeReparations = undefined;
  });

  describe('resolve', () => {
    it('should return IDemandeReparations returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDemandeReparations = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDemandeReparations).toEqual({ id: 123 });
    });

    it('should return new IDemandeReparations if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDemandeReparations = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDemandeReparations).toEqual(new DemandeReparations());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DemandeReparations })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDemandeReparations = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDemandeReparations).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
