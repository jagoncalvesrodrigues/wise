jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VentaService } from '../service/venta.service';
import { IVenta, Venta } from '../venta.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

import { VentaUpdateComponent } from './venta-update.component';

describe('Component Tests', () => {
  describe('Venta Management Update Component', () => {
    let comp: VentaUpdateComponent;
    let fixture: ComponentFixture<VentaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ventaService: VentaService;
    let usuarioService: UsuarioService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VentaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VentaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VentaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ventaService = TestBed.inject(VentaService);
      usuarioService = TestBed.inject(UsuarioService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Usuario query and add missing value', () => {
        const venta: IVenta = { id: 456 };
        const usuario: IUsuario = { id: 9403 };
        venta.usuario = usuario;

        const usuarioCollection: IUsuario[] = [{ id: 26601 }];
        spyOn(usuarioService, 'query').and.returnValue(of(new HttpResponse({ body: usuarioCollection })));
        const additionalUsuarios = [usuario];
        const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
        spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ venta });
        comp.ngOnInit();

        expect(usuarioService.query).toHaveBeenCalled();
        expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
        expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const venta: IVenta = { id: 456 };
        const usuario: IUsuario = { id: 64792 };
        venta.usuario = usuario;

        activatedRoute.data = of({ venta });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(venta));
        expect(comp.usuariosSharedCollection).toContain(usuario);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const venta = { id: 123 };
        spyOn(ventaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ venta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: venta }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ventaService.update).toHaveBeenCalledWith(venta);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const venta = new Venta();
        spyOn(ventaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ venta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: venta }));
        saveSubject.complete();

        // THEN
        expect(ventaService.create).toHaveBeenCalledWith(venta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const venta = { id: 123 };
        spyOn(ventaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ venta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ventaService.update).toHaveBeenCalledWith(venta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUsuarioById', () => {
        it('Should return tracked Usuario primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUsuarioById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
