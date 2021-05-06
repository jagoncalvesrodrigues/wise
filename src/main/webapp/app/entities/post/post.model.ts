import * as dayjs from 'dayjs';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IPost {
  id?: number;
  imagen?: string | null;
  like?: dayjs.Dayjs | null;
  usuario?: IUsuario | null;
}

export class Post implements IPost {
  constructor(public id?: number, public imagen?: string | null, public like?: dayjs.Dayjs | null, public usuario?: IUsuario | null) {}
}

export function getPostIdentifier(post: IPost): number | undefined {
  return post.id;
}
