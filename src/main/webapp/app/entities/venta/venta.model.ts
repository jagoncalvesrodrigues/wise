import * as dayjs from 'dayjs';
import { ICamiseta } from 'app/entities/camiseta/camiseta.model';
import { ISudadera } from 'app/entities/sudadera/sudadera.model';
import { IAccesorio } from 'app/entities/accesorio/accesorio.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IVenta {
  id?: number;
  importe?: number | null;
  fecha?: dayjs.Dayjs | null;
  camisetas?: ICamiseta[] | null;
  sudaderas?: ISudadera[] | null;
  accesorios?: IAccesorio[] | null;
  usuario?: IUsuario | null;
}

export class Venta implements IVenta {
  constructor(
    public id?: number,
    public importe?: number | null,
    public fecha?: dayjs.Dayjs | null,
    public camisetas?: ICamiseta[] | null,
    public sudaderas?: ISudadera[] | null,
    public accesorios?: IAccesorio[] | null,
    public usuario?: IUsuario | null
  ) {}
}

export function getVentaIdentifier(venta: IVenta): number | undefined {
  return venta.id;
}
