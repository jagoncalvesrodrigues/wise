import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAccesorio, getAccesorioIdentifier } from '../accesorio.model';

export type EntityResponseType = HttpResponse<IAccesorio>;
export type EntityArrayResponseType = HttpResponse<IAccesorio[]>;

@Injectable({ providedIn: 'root' })
export class AccesorioService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/accesorios');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(accesorio: IAccesorio): Observable<EntityResponseType> {
    return this.http.post<IAccesorio>(this.resourceUrl, accesorio, { observe: 'response' });
  }

  update(accesorio: IAccesorio): Observable<EntityResponseType> {
    return this.http.put<IAccesorio>(`${this.resourceUrl}/${getAccesorioIdentifier(accesorio) as number}`, accesorio, {
      observe: 'response',
    });
  }

  partialUpdate(accesorio: IAccesorio): Observable<EntityResponseType> {
    return this.http.patch<IAccesorio>(`${this.resourceUrl}/${getAccesorioIdentifier(accesorio) as number}`, accesorio, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAccesorio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccesorio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAccesorioToCollectionIfMissing(
    accesorioCollection: IAccesorio[],
    ...accesoriosToCheck: (IAccesorio | null | undefined)[]
  ): IAccesorio[] {
    const accesorios: IAccesorio[] = accesoriosToCheck.filter(isPresent);
    if (accesorios.length > 0) {
      const accesorioCollectionIdentifiers = accesorioCollection.map(accesorioItem => getAccesorioIdentifier(accesorioItem)!);
      const accesoriosToAdd = accesorios.filter(accesorioItem => {
        const accesorioIdentifier = getAccesorioIdentifier(accesorioItem);
        if (accesorioIdentifier == null || accesorioCollectionIdentifiers.includes(accesorioIdentifier)) {
          return false;
        }
        accesorioCollectionIdentifiers.push(accesorioIdentifier);
        return true;
      });
      return [...accesoriosToAdd, ...accesorioCollection];
    }
    return accesorioCollection;
  }
}
