import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISudadera, getSudaderaIdentifier } from '../sudadera.model';

export type EntityResponseType = HttpResponse<ISudadera>;
export type EntityArrayResponseType = HttpResponse<ISudadera[]>;

@Injectable({ providedIn: 'root' })
export class SudaderaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/sudaderas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(sudadera: ISudadera): Observable<EntityResponseType> {
    return this.http.post<ISudadera>(this.resourceUrl, sudadera, { observe: 'response' });
  }

  update(sudadera: ISudadera): Observable<EntityResponseType> {
    return this.http.put<ISudadera>(`${this.resourceUrl}/${getSudaderaIdentifier(sudadera) as number}`, sudadera, { observe: 'response' });
  }

  partialUpdate(sudadera: ISudadera): Observable<EntityResponseType> {
    return this.http.patch<ISudadera>(`${this.resourceUrl}/${getSudaderaIdentifier(sudadera) as number}`, sudadera, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISudadera>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISudadera[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSudaderaToCollectionIfMissing(sudaderaCollection: ISudadera[], ...sudaderasToCheck: (ISudadera | null | undefined)[]): ISudadera[] {
    const sudaderas: ISudadera[] = sudaderasToCheck.filter(isPresent);
    if (sudaderas.length > 0) {
      const sudaderaCollectionIdentifiers = sudaderaCollection.map(sudaderaItem => getSudaderaIdentifier(sudaderaItem)!);
      const sudaderasToAdd = sudaderas.filter(sudaderaItem => {
        const sudaderaIdentifier = getSudaderaIdentifier(sudaderaItem);
        if (sudaderaIdentifier == null || sudaderaCollectionIdentifiers.includes(sudaderaIdentifier)) {
          return false;
        }
        sudaderaCollectionIdentifiers.push(sudaderaIdentifier);
        return true;
      });
      return [...sudaderasToAdd, ...sudaderaCollection];
    }
    return sudaderaCollection;
  }
}
