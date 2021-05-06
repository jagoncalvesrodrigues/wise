import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccesorio, Accesorio } from '../accesorio.model';
import { AccesorioService } from '../service/accesorio.service';

@Injectable({ providedIn: 'root' })
export class AccesorioRoutingResolveService implements Resolve<IAccesorio> {
  constructor(protected service: AccesorioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAccesorio> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((accesorio: HttpResponse<Accesorio>) => {
          if (accesorio.body) {
            return of(accesorio.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Accesorio());
  }
}
