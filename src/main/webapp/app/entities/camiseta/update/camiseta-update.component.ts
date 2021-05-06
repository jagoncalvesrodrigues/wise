import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICamiseta, Camiseta } from '../camiseta.model';
import { CamisetaService } from '../service/camiseta.service';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';

@Component({
  selector: 'jhi-camiseta-update',
  templateUrl: './camiseta-update.component.html',
})
export class CamisetaUpdateComponent implements OnInit {
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
    protected camisetaService: CamisetaService,
    protected ventaService: VentaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ camiseta }) => {
      this.updateForm(camiseta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const camiseta = this.createFromForm();
    if (camiseta.id !== undefined) {
      this.subscribeToSaveResponse(this.camisetaService.update(camiseta));
    } else {
      this.subscribeToSaveResponse(this.camisetaService.create(camiseta));
    }
  }

  trackVentaById(index: number, item: IVenta): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICamiseta>>): void {
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

  protected updateForm(camiseta: ICamiseta): void {
    this.editForm.patchValue({
      id: camiseta.id,
      stock: camiseta.stock,
      imagen: camiseta.imagen,
      talla: camiseta.talla,
      color: camiseta.color,
      coleccion: camiseta.coleccion,
      venta: camiseta.venta,
    });

    this.ventasSharedCollection = this.ventaService.addVentaToCollectionIfMissing(this.ventasSharedCollection, camiseta.venta);
  }

  protected loadRelationshipsOptions(): void {
    this.ventaService
      .query()
      .pipe(map((res: HttpResponse<IVenta[]>) => res.body ?? []))
      .pipe(map((ventas: IVenta[]) => this.ventaService.addVentaToCollectionIfMissing(ventas, this.editForm.get('venta')!.value)))
      .subscribe((ventas: IVenta[]) => (this.ventasSharedCollection = ventas));
  }

  protected createFromForm(): ICamiseta {
    return {
      ...new Camiseta(),
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
