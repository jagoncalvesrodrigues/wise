jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAccesorio, Accesorio } from '../accesorio.model';
import { AccesorioService } from '../service/accesorio.service';

import { AccesorioRoutingResolveService } from './accesorio-routing-resolve.service';

describe('Service Tests', () => {
  describe('Accesorio routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AccesorioRoutingResolveService;
    let service: AccesorioService;
    let resultAccesorio: IAccesorio | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AccesorioRoutingResolveService);
      service = TestBed.inject(AccesorioService);
      resultAccesorio = undefined;
    });

    describe('resolve', () => {
      it('should return IAccesorio returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAccesorio = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAccesorio).toEqual({ id: 123 });
      });

      it('should return new IAccesorio if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAccesorio = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAccesorio).toEqual(new Accesorio());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAccesorio = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAccesorio).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
