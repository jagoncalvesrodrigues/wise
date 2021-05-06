import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CamisetaDetailComponent } from './camiseta-detail.component';

describe('Component Tests', () => {
  describe('Camiseta Management Detail Component', () => {
    let comp: CamisetaDetailComponent;
    let fixture: ComponentFixture<CamisetaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CamisetaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ camiseta: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CamisetaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CamisetaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load camiseta on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.camiseta).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
