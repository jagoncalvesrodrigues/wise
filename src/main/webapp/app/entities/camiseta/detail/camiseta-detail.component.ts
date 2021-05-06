import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICamiseta } from '../camiseta.model';

@Component({
  selector: 'jhi-camiseta-detail',
  templateUrl: './camiseta-detail.component.html',
})
export class CamisetaDetailComponent implements OnInit {
  camiseta: ICamiseta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ camiseta }) => {
      this.camiseta = camiseta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
