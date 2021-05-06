import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SudaderaDetailComponent } from './sudadera-detail.component';

describe('Component Tests', () => {
  describe('Sudadera Management Detail Component', () => {
    let comp: SudaderaDetailComponent;
    let fixture: ComponentFixture<SudaderaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SudaderaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ sudadera: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SudaderaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SudaderaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load sudadera on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.sudadera).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
