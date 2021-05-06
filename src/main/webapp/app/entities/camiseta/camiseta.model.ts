import { IVenta } from 'app/entities/venta/venta.model';

export interface ICamiseta {
  id?: number;
  stock?: number | null;
  imagen?: string | null;
  talla?: string | null;
  color?: string | null;
  coleccion?: number | null;
  venta?: IVenta | null;
}

export class Camiseta implements ICamiseta {
  constructor(
    public id?: number,
    public stock?: number | null,
    public imagen?: string | null,
    public talla?: string | null,
    public color?: string | null,
    public coleccion?: number | null,
    public venta?: IVenta | null
  ) {}
}

export function getCamisetaIdentifier(camiseta: ICamiseta): number | undefined {
  return camiseta.id;
}
