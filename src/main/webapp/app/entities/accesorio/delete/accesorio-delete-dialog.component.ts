import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAccesorio } from '../accesorio.model';
import { AccesorioService } from '../service/accesorio.service';

@Component({
  templateUrl: './accesorio-delete-dialog.component.html',
})
export class AccesorioDeleteDialogComponent {
  accesorio?: IAccesorio;

  constructor(protected accesorioService: AccesorioService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.accesorioService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
