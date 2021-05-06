import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISudadera, Sudadera } from '../sudadera.model';

import { SudaderaService } from './sudadera.service';

describe('Service Tests', () => {
  describe('Sudadera Service', () => {
    let service: SudaderaService;
    let httpMock: HttpTestingController;
    let elemDefault: ISudadera;
    let expectedResult: ISudadera | ISudadera[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SudaderaService);
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

      it('should create a Sudadera', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Sudadera()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Sudadera', () => {
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

      it('should partial update a Sudadera', () => {
        const patchObject = Object.assign(
          {
            stock: 1,
            talla: 'BBBBBB',
            color: 'BBBBBB',
            coleccion: 1,
          },
          new Sudadera()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Sudadera', () => {
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

      it('should delete a Sudadera', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSudaderaToCollectionIfMissing', () => {
        it('should add a Sudadera to an empty array', () => {
          const sudadera: ISudadera = { id: 123 };
          expectedResult = service.addSudaderaToCollectionIfMissing([], sudadera);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(sudadera);
        });

        it('should not add a Sudadera to an array that contains it', () => {
          const sudadera: ISudadera = { id: 123 };
          const sudaderaCollection: ISudadera[] = [
            {
              ...sudadera,
            },
            { id: 456 },
          ];
          expectedResult = service.addSudaderaToCollectionIfMissing(sudaderaCollection, sudadera);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Sudadera to an array that doesn't contain it", () => {
          const sudadera: ISudadera = { id: 123 };
          const sudaderaCollection: ISudadera[] = [{ id: 456 }];
          expectedResult = service.addSudaderaToCollectionIfMissing(sudaderaCollection, sudadera);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(sudadera);
        });

        it('should add only unique Sudadera to an array', () => {
          const sudaderaArray: ISudadera[] = [{ id: 123 }, { id: 456 }, { id: 74006 }];
          const sudaderaCollection: ISudadera[] = [{ id: 123 }];
          expectedResult = service.addSudaderaToCollectionIfMissing(sudaderaCollection, ...sudaderaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const sudadera: ISudadera = { id: 123 };
          const sudadera2: ISudadera = { id: 456 };
          expectedResult = service.addSudaderaToCollectionIfMissing([], sudadera, sudadera2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(sudadera);
          expect(expectedResult).toContain(sudadera2);
        });

        it('should accept null and undefined values', () => {
          const sudadera: ISudadera = { id: 123 };
          expectedResult = service.addSudaderaToCollectionIfMissing([], null, sudadera, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(sudadera);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
