import { IVenta } from 'app/entities/venta/venta.model';

export interface IAccesorio {
  id?: number;
  stock?: number | null;
  imagen?: string | null;
  talla?: string | null;
  color?: string | null;
  coleccion?: number | null;
  venta?: IVenta | null;
}

export class Accesorio implements IAccesorio {
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

export function getAccesorioIdentifier(accesorio: IAccesorio): number | undefined {
  return accesorio.id;
}
