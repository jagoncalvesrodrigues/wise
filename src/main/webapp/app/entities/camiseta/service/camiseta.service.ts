import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICamiseta, getCamisetaIdentifier } from '../camiseta.model';

export type EntityResponseType = HttpResponse<ICamiseta>;
export type EntityArrayResponseType = HttpResponse<ICamiseta[]>;

@Injectable({ providedIn: 'root' })
export class CamisetaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/camisetas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(camiseta: ICamiseta): Observable<EntityResponseType> {
    return this.http.post<ICamiseta>(this.resourceUrl, camiseta, { observe: 'response' });
  }

  update(camiseta: ICamiseta): Observable<EntityResponseType> {
    return this.http.put<ICamiseta>(`${this.resourceUrl}/${getCamisetaIdentifier(camiseta) as number}`, camiseta, { observe: 'response' });
  }

  partialUpdate(camiseta: ICamiseta): Observable<EntityResponseType> {
    return this.http.patch<ICamiseta>(`${this.resourceUrl}/${getCamisetaIdentifier(camiseta) as number}`, camiseta, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICamiseta>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICamiseta[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCamisetaToCollectionIfMissing(camisetaCollection: ICamiseta[], ...camisetasToCheck: (ICamiseta | null | undefined)[]): ICamiseta[] {
    const camisetas: ICamiseta[] = camisetasToCheck.filter(isPresent);
    if (camisetas.length > 0) {
      const camisetaCollectionIdentifiers = camisetaCollection.map(camisetaItem => getCamisetaIdentifier(camisetaItem)!);
      const camisetasToAdd = camisetas.filter(camisetaItem => {
        const camisetaIdentifier = getCamisetaIdentifier(camisetaItem);
        if (camisetaIdentifier == null || camisetaCollectionIdentifiers.includes(camisetaIdentifier)) {
          return false;
        }
        camisetaCollectionIdentifiers.push(camisetaIdentifier);
        return true;
      });
      return [...camisetasToAdd, ...camisetaCollection];
    }
    return camisetaCollection;
  }
}
