import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISudadera, Sudadera } from '../sudadera.model';
import { SudaderaService } from '../service/sudadera.service';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';

@Component({
  selector: 'jhi-sudadera-update',
  templateUrl: './sudadera-update.component.html',
})
export class SudaderaUpdateComponent implements OnInit {
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
    protected sudaderaService: SudaderaService,
    protected ventaService: VentaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sudadera }) => {
      this.updateForm(sudadera);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sudadera = this.createFromForm();
    if (sudadera.id !== undefined) {
      this.subscribeToSaveResponse(this.sudaderaService.update(sudadera));
    } else {
      this.subscribeToSaveResponse(this.sudaderaService.create(sudadera));
    }
  }

  trackVentaById(index: number, item: IVenta): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISudadera>>): void {
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

  protected updateForm(sudadera: ISudadera): void {
    this.editForm.patchValue({
      id: sudadera.id,
      stock: sudadera.stock,
      imagen: sudadera.imagen,
      talla: sudadera.talla,
      color: sudadera.color,
      coleccion: sudadera.coleccion,
      venta: sudadera.venta,
    });

    this.ventasSharedCollection = this.ventaService.addVentaToCollectionIfMissing(this.ventasSharedCollection, sudadera.venta);
  }

  protected loadRelationshipsOptions(): void {
    this.ventaService
      .query()
      .pipe(map((res: HttpResponse<IVenta[]>) => res.body ?? []))
      .pipe(map((ventas: IVenta[]) => this.ventaService.addVentaToCollectionIfMissing(ventas, this.editForm.get('venta')!.value)))
      .subscribe((ventas: IVenta[]) => (this.ventasSharedCollection = ventas));
  }

  protected createFromForm(): ISudadera {
    return {
      ...new Sudadera(),
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
