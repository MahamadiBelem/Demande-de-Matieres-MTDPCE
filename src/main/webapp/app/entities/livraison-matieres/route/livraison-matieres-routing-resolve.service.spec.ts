import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ILivraisonMatieres, LivraisonMatieres } from '../livraison-matieres.model';
import { LivraisonMatieresService } from '../service/livraison-matieres.service';

import { LivraisonMatieresRoutingResolveService } from './livraison-matieres-routing-resolve.service';

describe('LivraisonMatieres routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LivraisonMatieresRoutingResolveService;
  let service: LivraisonMatieresService;
  let resultLivraisonMatieres: ILivraisonMatieres | undefined;

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
    routingResolveService = TestBed.inject(LivraisonMatieresRoutingResolveService);
    service = TestBed.inject(LivraisonMatieresService);
    resultLivraisonMatieres = undefined;
  });

  describe('resolve', () => {
    it('should return ILivraisonMatieres returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLivraisonMatieres = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLivraisonMatieres).toEqual({ id: 123 });
    });

    it('should return new ILivraisonMatieres if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLivraisonMatieres = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLivraisonMatieres).toEqual(new LivraisonMatieres());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LivraisonMatieres })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLivraisonMatieres = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLivraisonMatieres).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
