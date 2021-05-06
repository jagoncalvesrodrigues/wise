jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISudadera, Sudadera } from '../sudadera.model';
import { SudaderaService } from '../service/sudadera.service';

import { SudaderaRoutingResolveService } from './sudadera-routing-resolve.service';

describe('Service Tests', () => {
  describe('Sudadera routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SudaderaRoutingResolveService;
    let service: SudaderaService;
    let resultSudadera: ISudadera | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SudaderaRoutingResolveService);
      service = TestBed.inject(SudaderaService);
      resultSudadera = undefined;
    });

    describe('resolve', () => {
      it('should return ISudadera returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSudadera = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSudadera).toEqual({ id: 123 });
      });

      it('should return new ISudadera if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSudadera = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSudadera).toEqual(new Sudadera());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSudadera = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSudadera).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
