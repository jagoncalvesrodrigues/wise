import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAccesorio, Accesorio } from '../accesorio.model';
import { AccesorioService } from '../service/accesorio.service';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';

@Component({
  selector: 'jhi-accesorio-update',
  templateUrl: './accesorio-update.component.html',
})
export class AccesorioUpdateComponent implements OnInit {
  isSaving = false;

  ventasSharedCollection: IVenta[] = [];

  editForm = this.fb.group({
    id: [],
    stock: [],
    imagen: [],
    talla: [],
    color: [],
    coleccion: [],
    venta: [],
  });

  constructor(
    protected accesorioService: AccesorioService,
    protected ventaService: VentaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accesorio }) => {
      this.updateForm(accesorio);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accesorio = this.createFromForm();
    if (accesorio.id !== undefined) {
      this.subscribeToSaveResponse(this.accesorioService.update(accesorio));
    } else {
      this.subscribeToSaveResponse(this.accesorioService.create(accesorio));
    }
  }

  trackVentaById(index: number, item: IVenta): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccesorio>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(accesorio: IAccesorio): void {
    this.editForm.patchValue({
      id: accesorio.id,
      stock: accesorio.stock,
      imagen: accesorio.imagen,
      talla: accesorio.talla,
      color: accesorio.color,
      coleccion: accesorio.coleccion,
      venta: accesorio.venta,
    });

    this.ventasSharedCollection = this.ventaService.addVentaToCollectionIfMissing(this.ventasSharedCollection, accesorio.venta);
  }

  protected loadRelationshipsOptions(): void {
    this.ventaService
      .query()
      .pipe(map((res: HttpResponse<IVenta[]>) => res.body ?? []))
      .pipe(map((ventas: IVenta[]) => this.ventaService.addVentaToCollectionIfMissing(ventas, this.editForm.get('venta')!.value)))
      .subscribe((ventas: IVenta[]) => (this.ventasSharedCollection = ventas));
  }

  protected createFromForm(): IAccesorio {
    return {
      ...new Accesorio(),
      id: this.editForm.get(['id'])!.value,
      stock: this.editForm.get(['stock'])!.value,
      imagen: this.editForm.get(['imagen'])!.value,
      talla: this.editForm.get(['talla'])!.value,
      color: this.editForm.get(['color'])!.value,
      coleccion: this.editForm.get(['coleccion'])!.value,
      venta: this.editForm.get(['venta'])!.value,
    };
  }
}
