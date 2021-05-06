import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'usuario',
        data: { pageTitle: 'wiseApp.usuario.home.title' },
        loadChildren: () => import('./usuario/usuario.module').then(m => m.UsuarioModule),
      },
      {
        path: 'camiseta',
        data: { pageTitle: 'wiseApp.camiseta.home.title' },
        loadChildren: () => import('./camiseta/camiseta.module').then(m => m.CamisetaModule),
      },
      {
        path: 'sudadera',
        data: { pageTitle: 'wiseApp.sudadera.home.title' },
        loadChildren: () => import('./sudadera/sudadera.module').then(m => m.SudaderaModule),
      },
      {
        path: 'accesorio',
        data: { pageTitle: 'wiseApp.accesorio.home.title' },
        loadChildren: () => import('./accesorio/accesorio.module').then(m => m.AccesorioModule),
      },
      {
        path: 'venta',
        data: { pageTitle: 'wiseApp.venta.home.title' },
        loadChildren: () => import('./venta/venta.module').then(m => m.VentaModule),
      },
      {
        path: 'post',
        data: { pageTitle: 'wiseApp.post.home.title' },
        loadChildren: () => import('./post/post.module').then(m => m.PostModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
