import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICamiseta, Camiseta } from '../camiseta.model';
import { CamisetaService } from '../service/camiseta.service';

@Injectable({ providedIn: 'root' })
export class CamisetaRoutingResolveService implements Resolve<ICamiseta> {
  constructor(protected service: CamisetaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICamiseta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((camiseta: HttpResponse<Camiseta>) => {
          if (camiseta.body) {
            return of(camiseta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Camiseta());
  }
}
