import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SudaderaComponent } from '../list/sudadera.component';
import { SudaderaDetailComponent } from '../detail/sudadera-detail.component';
import { SudaderaUpdateComponent } from '../update/sudadera-update.component';
import { SudaderaRoutingResolveService } from './sudadera-routing-resolve.service';

const sudaderaRoute: Routes = [
  {
    path: '',
    component: SudaderaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SudaderaDetailComponent,
    resolve: {
      sudadera: SudaderaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SudaderaUpdateComponent,
    resolve: {
      sudadera: SudaderaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SudaderaUpdateComponent,
    resolve: {
      sudadera: SudaderaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sudaderaRoute)],
  exports: [RouterModule],
})
export class SudaderaRoutingModule {}
