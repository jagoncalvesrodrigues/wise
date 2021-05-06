import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AccesorioComponent } from './list/accesorio.component';
import { AccesorioDetailComponent } from './detail/accesorio-detail.component';
import { AccesorioUpdateComponent } from './update/accesorio-update.component';
import { AccesorioDeleteDialogComponent } from './delete/accesorio-delete-dialog.component';
import { AccesorioRoutingModule } from './route/accesorio-routing.module';

@NgModule({
  imports: [SharedModule, AccesorioRoutingModule],
  declarations: [AccesorioComponent, AccesorioDetailComponent, AccesorioUpdateComponent, AccesorioDeleteDialogComponent],
  entryComponents: [AccesorioDeleteDialogComponent],
})
export class AccesorioModule {}
