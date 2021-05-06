import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICamiseta } from '../camiseta.model';
import { CamisetaService } from '../service/camiseta.service';

@Component({
  templateUrl: './camiseta-delete-dialog.component.html',
})
export class CamisetaDeleteDialogComponent {
  camiseta?: ICamiseta;

  constructor(protected camisetaService: CamisetaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.camisetaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
