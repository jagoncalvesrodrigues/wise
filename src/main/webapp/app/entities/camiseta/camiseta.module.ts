import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CamisetaComponent } from './list/camiseta.component';
import { CamisetaDetailComponent } from './detail/camiseta-detail.component';
import { CamisetaUpdateComponent } from './update/camiseta-update.component';
import { CamisetaDeleteDialogComponent } from './delete/camiseta-delete-dialog.component';
import { CamisetaRoutingModule } from './route/camiseta-routing.module';

@NgModule({
  imports: [SharedModule, CamisetaRoutingModule],
  declarations: [CamisetaComponent, CamisetaDetailComponent, CamisetaUpdateComponent, CamisetaDeleteDialogComponent],
  entryComponents: [CamisetaDeleteDialogComponent],
})
export class CamisetaModule {}
