import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISudadera, Sudadera } from '../sudadera.model';
import { SudaderaService } from '../service/sudadera.service';

@Injectable({ providedIn: 'root' })
export class SudaderaRoutingResolveService implements Resolve<ISudadera> {
  constructor(protected service: SudaderaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISudadera> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sudadera: HttpResponse<Sudadera>) => {
          if (sudadera.body) {
            return of(sudadera.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Sudadera());
  }
}
