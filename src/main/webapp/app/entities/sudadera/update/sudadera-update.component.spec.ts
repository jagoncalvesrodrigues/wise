jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SudaderaService } from '../service/sudadera.service';
import { ISudadera, Sudadera } from '../sudadera.model';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';

import { SudaderaUpdateComponent } from './sudadera-update.component';

describe('Component Tests', () => {
  describe('Sudadera Management Update Component', () => {
    let comp: SudaderaUpdateComponent;
    let fixture: ComponentFixture<SudaderaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let sudaderaService: SudaderaService;
    let ventaService: VentaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SudaderaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SudaderaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SudaderaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      sudaderaService = TestBed.inject(SudaderaService);
      ventaService = TestBed.inject(VentaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Venta query and add missing value', () => {
        const sudadera: ISudadera = { id: 456 };
        const venta: IVenta = { id: 26694 };
        sudadera.venta = venta;

        const ventaCollection: IVenta[] = [{ id: 78982 }];
        spyOn(ventaService, 'query').and.returnValue(of(new HttpResponse({ body: ventaCollection })));
        const additionalVentas = [venta];
        const expectedCollection: IVenta[] = [...additionalVentas, ...ventaCollection];
        spyOn(ventaService, 'addVentaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ sudadera });
        comp.ngOnInit();

        expect(ventaService.query).toHaveBeenCalled();
        expect(ventaService.addVentaToCollectionIfMissing).toHaveBeenCalledWith(ventaCollection, ...additionalVentas);
        expect(comp.ventasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const sudadera: ISudadera = { id: 456 };
        const venta: IVenta = { id: 10961 };
        sudadera.venta = venta;

        activatedRoute.data = of({ sudadera });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(sudadera));
        expect(comp.ventasSharedCollection).toContain(venta);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const sudadera = { id: 123 };
        spyOn(sudaderaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ sudadera });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: sudadera }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(sudaderaService.update).toHaveBeenCalledWith(sudadera);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const sudadera = new Sudadera();
        spyOn(sudaderaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ sudadera });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: sudadera }));
        saveSubject.complete();

        // THEN
        expect(sudaderaService.create).toHaveBeenCalledWith(sudadera);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const sudadera = { id: 123 };
        spyOn(sudaderaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ sudadera });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(sudaderaService.update).toHaveBeenCalledWith(sudadera);
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
