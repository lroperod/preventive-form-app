import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormAnswerDetailComponent } from './form-answer-detail.component';

describe('Component Tests', () => {
  describe('FormAnswer Management Detail Component', () => {
    let comp: FormAnswerDetailComponent;
    let fixture: ComponentFixture<FormAnswerDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FormAnswerDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ formAnswer: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FormAnswerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FormAnswerDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load formAnswer on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.formAnswer).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
