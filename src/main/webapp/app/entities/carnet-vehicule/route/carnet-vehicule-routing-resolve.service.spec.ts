import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICarnetVehicule, CarnetVehicule } from '../carnet-vehicule.model';
import { CarnetVehiculeService } from '../service/carnet-vehicule.service';

import { CarnetVehiculeRoutingResolveService } from './carnet-vehicule-routing-resolve.service';

describe('CarnetVehicule routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CarnetVehiculeRoutingResolveService;
  let service: CarnetVehiculeService;
  let resultCarnetVehicule: ICarnetVehicule | undefined;

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
    routingResolveService = TestBed.inject(CarnetVehiculeRoutingResolveService);
    service = TestBed.inject(CarnetVehiculeService);
    resultCarnetVehicule = undefined;
  });

  describe('resolve', () => {
    it('should return ICarnetVehicule returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCarnetVehicule = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCarnetVehicule).toEqual({ id: 123 });
    });

    it('should return new ICarnetVehicule if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCarnetVehicule = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCarnetVehicule).toEqual(new CarnetVehicule());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CarnetVehicule })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCarnetVehicule = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCarnetVehicule).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
