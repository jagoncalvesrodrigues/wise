import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AccesorioComponent } from '../list/accesorio.component';
import { AccesorioDetailComponent } from '../detail/accesorio-detail.component';
import { AccesorioUpdateComponent } from '../update/accesorio-update.component';
import { AccesorioRoutingResolveService } from './accesorio-routing-resolve.service';

const accesorioRoute: Routes = [
  {
    path: '',
    component: AccesorioComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccesorioDetailComponent,
    resolve: {
      accesorio: AccesorioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccesorioUpdateComponent,
    resolve: {
      accesorio: AccesorioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccesorioUpdateComponent,
    resolve: {
      accesorio: AccesorioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(accesorioRoute)],
  exports: [RouterModule],
})
export class AccesorioRoutingModule {}
