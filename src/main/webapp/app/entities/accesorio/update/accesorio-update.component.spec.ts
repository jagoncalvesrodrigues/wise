jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AccesorioService } from '../service/accesorio.service';
import { IAccesorio, Accesorio } from '../accesorio.model';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';

import { AccesorioUpdateComponent } from './accesorio-update.component';

describe('Component Tests', () => {
  describe('Accesorio Management Update Component', () => {
    let comp: AccesorioUpdateComponent;
    let fixture: ComponentFixture<AccesorioUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let accesorioService: AccesorioService;
    let ventaService: VentaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AccesorioUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AccesorioUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AccesorioUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      accesorioService = TestBed.inject(AccesorioService);
      ventaService = TestBed.inject(VentaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Venta query and add missing value', () => {
        const accesorio: IAccesorio = { id: 456 };
        const venta: IVenta = { id: 77300 };
        accesorio.venta = venta;

        const ventaCollection: IVenta[] = [{ id: 91228 }];
        spyOn(ventaService, 'query').and.returnValue(of(new HttpResponse({ body: ventaCollection })));
        const additionalVentas = [venta];
        const expectedCollection: IVenta[] = [...additionalVentas, ...ventaCollection];
        spyOn(ventaService, 'addVentaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ accesorio });
        comp.ngOnInit();

        expect(ventaService.query).toHaveBeenCalled();
        expect(ventaService.addVentaToCollectionIfMissing).toHaveBeenCalledWith(ventaCollection, ...additionalVentas);
        expect(comp.ventasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const accesorio: IAccesorio = { id: 456 };
        const venta: IVenta = { id: 1440 };
        accesorio.venta = venta;

        activatedRoute.data = of({ accesorio });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(accesorio));
        expect(comp.ventasSharedCollection).toContain(venta);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const accesorio = { id: 123 };
        spyOn(accesorioService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ accesorio });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: accesorio }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(accesorioService.update).toHaveBeenCalledWith(accesorio);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const accesorio = new Accesorio();
        spyOn(accesorioService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ accesorio });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: accesorio }));
        saveSubject.complete();

        // THEN
        expect(accesorioService.create).toHaveBeenCalledWith(accesorio);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const accesorio = { id: 123 };
        spyOn(accesorioService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ accesorio });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(accesorioService.update).toHaveBeenCalledWith(accesorio);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackVentaById', () => {
        it('Should return tracked Venta primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVentaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
