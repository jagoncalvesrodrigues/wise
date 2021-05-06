jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CamisetaService } from '../service/camiseta.service';
import { ICamiseta, Camiseta } from '../camiseta.model';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';

import { CamisetaUpdateComponent } from './camiseta-update.component';

describe('Component Tests', () => {
  describe('Camiseta Management Update Component', () => {
    let comp: CamisetaUpdateComponent;
    let fixture: ComponentFixture<CamisetaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let camisetaService: CamisetaService;
    let ventaService: VentaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CamisetaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CamisetaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CamisetaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      camisetaService = TestBed.inject(CamisetaService);
      ventaService = TestBed.inject(VentaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Venta query and add missing value', () => {
        const camiseta: ICamiseta = { id: 456 };
        const venta: IVenta = { id: 62066 };
        camiseta.venta = venta;

        const ventaCollection: IVenta[] = [{ id: 16754 }];
        spyOn(ventaService, 'query').and.returnValue(of(new HttpResponse({ body: ventaCollection })));
        const additionalVentas = [venta];
        const expectedCollection: IVenta[] = [...additionalVentas, ...ventaCollection];
        spyOn(ventaService, 'addVentaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ camiseta });
        comp.ngOnInit();

        expect(ventaService.query).toHaveBeenCalled();
        expect(ventaService.addVentaToCollectionIfMissing).toHaveBeenCalledWith(ventaCollection, ...additionalVentas);
        expect(comp.ventasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const camiseta: ICamiseta = { id: 456 };
        const venta: IVenta = { id: 9111 };
        camiseta.venta = venta;

        activatedRoute.data = of({ camiseta });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(camiseta));
        expect(comp.ventasSharedCollection).toContain(venta);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const camiseta = { id: 123 };
        spyOn(camisetaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ camiseta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: camiseta }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(camisetaService.update).toHaveBeenCalledWith(camiseta);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const camiseta = new Camiseta();
        spyOn(camisetaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ camiseta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: camiseta }));
        saveSubject.complete();

        // THEN
        expect(camisetaService.create).toHaveBeenCalledWith(camiseta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const camiseta = { id: 123 };
        spyOn(camisetaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ camiseta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(camisetaService.update).toHaveBeenCalledWith(camiseta);
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
