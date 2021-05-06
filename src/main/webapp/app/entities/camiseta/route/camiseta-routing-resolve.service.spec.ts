jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICamiseta, Camiseta } from '../camiseta.model';
import { CamisetaService } from '../service/camiseta.service';

import { CamisetaRoutingResolveService } from './camiseta-routing-resolve.service';

describe('Service Tests', () => {
  describe('Camiseta routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CamisetaRoutingResolveService;
    let service: CamisetaService;
    let resultCamiseta: ICamiseta | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CamisetaRoutingResolveService);
      service = TestBed.inject(CamisetaService);
      resultCamiseta = undefined;
    });

    describe('resolve', () => {
      it('should return ICamiseta returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCamiseta = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCamiseta).toEqual({ id: 123 });
      });

      it('should return new ICamiseta if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCamiseta = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCamiseta).toEqual(new Camiseta());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCamiseta = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCamiseta).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
