import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AccesorioDetailComponent } from './accesorio-detail.component';

describe('Component Tests', () => {
  describe('Accesorio Management Detail Component', () => {
    let comp: AccesorioDetailComponent;
    let fixture: ComponentFixture<AccesorioDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AccesorioDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ accesorio: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AccesorioDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AccesorioDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load accesorio on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.accesorio).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
