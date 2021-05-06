import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CamisetaComponent } from '../list/camiseta.component';
import { CamisetaDetailComponent } from '../detail/camiseta-detail.component';
import { CamisetaUpdateComponent } from '../update/camiseta-update.component';
import { CamisetaRoutingResolveService } from './camiseta-routing-resolve.service';

const camisetaRoute: Routes = [
  {
    path: '',
    component: CamisetaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CamisetaDetailComponent,
    resolve: {
      camiseta: CamisetaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CamisetaUpdateComponent,
    resolve: {
      camiseta: CamisetaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CamisetaUpdateComponent,
    resolve: {
      camiseta: CamisetaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(camisetaRoute)],
  exports: [RouterModule],
})
export class CamisetaRoutingModule {}
