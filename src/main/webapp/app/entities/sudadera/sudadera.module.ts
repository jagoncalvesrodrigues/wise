import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SudaderaComponent } from './list/sudadera.component';
import { SudaderaDetailComponent } from './detail/sudadera-detail.component';
import { SudaderaUpdateComponent } from './update/sudadera-update.component';
import { SudaderaDeleteDialogComponent } from './delete/sudadera-delete-dialog.component';
import { SudaderaRoutingModule } from './route/sudadera-routing.module';

@NgModule({
  imports: [SharedModule, SudaderaRoutingModule],
  declarations: [SudaderaComponent, SudaderaDetailComponent, SudaderaUpdateComponent, SudaderaDeleteDialogComponent],
  entryComponents: [SudaderaDeleteDialogComponent],
})
export class SudaderaModule {}
