import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IMarqueVehicule, MarqueVehicule } from '../marque-vehicule.model';
import { MarqueVehiculeService } from '../service/marque-vehicule.service';

import { MarqueVehiculeRoutingResolveService } from './marque-vehicule-routing-resolve.service';

describe('MarqueVehicule routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: MarqueVehiculeRoutingResolveService;
  let service: MarqueVehiculeService;
  let resultMarqueVehicule: IMarqueVehicule | undefined;

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
    routingResolveService = TestBed.inject(MarqueVehiculeRoutingResolveService);
    service = TestBed.inject(MarqueVehiculeService);
    resultMarqueVehicule = undefined;
  });

  describe('resolve', () => {
    it('should return IMarqueVehicule returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMarqueVehicule = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMarqueVehicule).toEqual({ id: 123 });
    });

    it('should return new IMarqueVehicule if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMarqueVehicule = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultMarqueVehicule).toEqual(new MarqueVehicule());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as MarqueVehicule })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMarqueVehicule = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMarqueVehicule).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
