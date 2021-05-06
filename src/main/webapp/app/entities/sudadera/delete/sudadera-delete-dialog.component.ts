import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISudadera } from '../sudadera.model';
import { SudaderaService } from '../service/sudadera.service';

@Component({
  templateUrl: './sudadera-delete-dialog.component.html',
})
export class SudaderaDeleteDialogComponent {
  sudadera?: ISudadera;

  constructor(protected sudaderaService: SudaderaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sudaderaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
