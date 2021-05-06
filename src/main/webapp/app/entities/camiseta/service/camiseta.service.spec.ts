import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICamiseta, Camiseta } from '../camiseta.model';

import { CamisetaService } from './camiseta.service';

describe('Service Tests', () => {
  describe('Camiseta Service', () => {
    let service: CamisetaService;
    let httpMock: HttpTestingController;
    let elemDefault: ICamiseta;
    let expectedResult: ICamiseta | ICamiseta[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CamisetaService);
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

      it('should create a Camiseta', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Camiseta()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Camiseta', () => {
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

      it('should partial update a Camiseta', () => {
        const patchObject = Object.assign(
          {
            imagen: 'BBBBBB',
          },
          new Camiseta()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Camiseta', () => {
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

      it('should delete a Camiseta', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCamisetaToCollectionIfMissing', () => {
        it('should add a Camiseta to an empty array', () => {
          const camiseta: ICamiseta = { id: 123 };
          expectedResult = service.addCamisetaToCollectionIfMissing([], camiseta);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(camiseta);
        });

        it('should not add a Camiseta to an array that contains it', () => {
          const camiseta: ICamiseta = { id: 123 };
          const camisetaCollection: ICamiseta[] = [
            {
              ...camiseta,
            },
            { id: 456 },
          ];
          expectedResult = service.addCamisetaToCollectionIfMissing(camisetaCollection, camiseta);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Camiseta to an array that doesn't contain it", () => {
          const camiseta: ICamiseta = { id: 123 };
          const camisetaCollection: ICamiseta[] = [{ id: 456 }];
          expectedResult = service.addCamisetaToCollectionIfMissing(camisetaCollection, camiseta);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(camiseta);
        });

        it('should add only unique Camiseta to an array', () => {
          const camisetaArray: ICamiseta[] = [{ id: 123 }, { id: 456 }, { id: 35492 }];
          const camisetaCollection: ICamiseta[] = [{ id: 123 }];
          expectedResult = service.addCamisetaToCollectionIfMissing(camisetaCollection, ...camisetaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const camiseta: ICamiseta = { id: 123 };
          const camiseta2: ICamiseta = { id: 456 };
          expectedResult = service.addCamisetaToCollectionIfMissing([], camiseta, camiseta2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(camiseta);
          expect(expectedResult).toContain(camiseta2);
        });

        it('should accept null and undefined values', () => {
          const camiseta: ICamiseta = { id: 123 };
          expectedResult = service.addCamisetaToCollectionIfMissing([], null, camiseta, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(camiseta);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
