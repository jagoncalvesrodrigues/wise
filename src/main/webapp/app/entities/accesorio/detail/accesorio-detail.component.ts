import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAccesorio } from '../accesorio.model';

@Component({
  selector: 'jhi-accesorio-detail',
  templateUrl: './accesorio-detail.component.html',
})
export class AccesorioDetailComponent implements OnInit {
  accesorio: IAccesorio | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accesorio }) => {
      this.accesorio = accesorio;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
