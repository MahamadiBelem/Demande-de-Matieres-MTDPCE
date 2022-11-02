import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IRevisionVehicule, RevisionVehicule } from '../revision-vehicule.model';
import { RevisionVehiculeService } from '../service/revision-vehicule.service';

import { RevisionVehiculeRoutingResolveService } from './revision-vehicule-routing-resolve.service';

describe('RevisionVehicule routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RevisionVehiculeRoutingResolveService;
  let service: RevisionVehiculeService;
  let resultRevisionVehicule: IRevisionVehicule | undefined;

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
    routingResolveService = TestBed.inject(RevisionVehiculeRoutingResolveService);
    service = TestBed.inject(RevisionVehiculeService);
    resultRevisionVehicule = undefined;
  });

  describe('resolve', () => {
    it('should return IRevisionVehicule returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRevisionVehicule = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRevisionVehicule).toEqual({ id: 123 });
    });

    it('should return new IRevisionVehicule if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRevisionVehicule = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRevisionVehicule).toEqual(new RevisionVehicule());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RevisionVehicule })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRevisionVehicule = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRevisionVehicule).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
