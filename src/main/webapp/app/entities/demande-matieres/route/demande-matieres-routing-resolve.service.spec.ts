import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IDemandeMatieres, DemandeMatieres } from '../demande-matieres.model';
import { DemandeMatieresService } from '../service/demande-matieres.service';

import { DemandeMatieresRoutingResolveService } from './demande-matieres-routing-resolve.service';

describe('DemandeMatieres routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DemandeMatieresRoutingResolveService;
  let service: DemandeMatieresService;
  let resultDemandeMatieres: IDemandeMatieres | undefined;

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
    routingResolveService = TestBed.inject(DemandeMatieresRoutingResolveService);
    service = TestBed.inject(DemandeMatieresService);
    resultDemandeMatieres = undefined;
  });

  describe('resolve', () => {
    it('should return IDemandeMatieres returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDemandeMatieres = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDemandeMatieres).toEqual({ id: 123 });
    });

    it('should return new IDemandeMatieres if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDemandeMatieres = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDemandeMatieres).toEqual(new DemandeMatieres());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DemandeMatieres })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDemandeMatieres = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDemandeMatieres).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
