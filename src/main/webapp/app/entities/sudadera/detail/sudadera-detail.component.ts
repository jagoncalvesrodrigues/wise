import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISudadera } from '../sudadera.model';

@Component({
  selector: 'jhi-sudadera-detail',
  templateUrl: './sudadera-detail.component.html',
})
export class SudaderaDetailComponent implements OnInit {
  sudadera: ISudadera | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sudadera }) => {
      this.sudadera = sudadera;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
