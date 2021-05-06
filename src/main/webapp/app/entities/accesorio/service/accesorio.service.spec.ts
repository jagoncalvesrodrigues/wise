import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAccesorio, Accesorio } from '../accesorio.model';

import { AccesorioService } from './accesorio.service';

describe('Service Tests', () => {
  describe('Accesorio Service', () => {
    let service: AccesorioService;
    let httpMock: HttpTestingController;
    let elemDefault: IAccesorio;
    let expectedResult: IAccesorio | IAccesorio[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AccesorioService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        stock: 0,
        imagen: 'AAAAAAA',
        talla: 'AAAAAAA',
        color: 'AAAAAAA',
        coleccion: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Accesorio', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Accesorio()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Accesorio', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            stock: 1,
            imagen: 'BBBBBB',
            talla: 'BBBBBB',
            color: 'BBBBBB',
            coleccion: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Accesorio', () => {
        const patchObject = Object.assign(
          {
            imagen: 'BBBBBB',
          },
          new Accesorio()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Accesorio', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            stock: 1,
            imagen: 'BBBBBB',
            talla: 'BBBBBB',
            color: 'BBBBBB',
            coleccion: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Accesorio', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAccesorioToCollectionIfMissing', () => {
        it('should add a Accesorio to an empty array', () => {
          const accesorio: IAccesorio = { id: 123 };
          expectedResult = service.addAccesorioToCollectionIfMissing([], accesorio);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(accesorio);
        });

        it('should not add a Accesorio to an array that contains it', () => {
          const accesorio: IAccesorio = { id: 123 };
          const accesorioCollection: IAccesorio[] = [
            {
              ...accesorio,
            },
            { id: 456 },
          ];
          expectedResult = service.addAccesorioToCollectionIfMissing(accesorioCollection, accesorio);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Accesorio to an array that doesn't contain it", () => {
          const accesorio: IAccesorio = { id: 123 };
          const accesorioCollection: IAccesorio[] = [{ id: 456 }];
          expectedResult = service.addAccesorioToCollectionIfMissing(accesorioCollection, accesorio);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(accesorio);
        });

        it('should add only unique Accesorio to an array', () => {
          const accesorioArray: IAccesorio[] = [{ id: 123 }, { id: 456 }, { id: 86987 }];
          const accesorioCollection: IAccesorio[] = [{ id: 123 }];
          expectedResult = service.addAccesorioToCollectionIfMissing(accesorioCollection, ...accesorioArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const accesorio: IAccesorio = { id: 123 };
          const accesorio2: IAccesorio = { id: 456 };
          expectedResult = service.addAccesorioToCollectionIfMissing([], accesorio, accesorio2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(accesorio);
          expect(expectedResult).toContain(accesorio2);
        });

        it('should accept null and undefined values', () => {
          const accesorio: IAccesorio = { id: 123 };
          expectedResult = service.addAccesorioToCollectionIfMissing([], null, accesorio, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(accesorio);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
