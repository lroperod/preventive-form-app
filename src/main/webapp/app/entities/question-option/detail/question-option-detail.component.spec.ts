import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { QuestionOptionDetailComponent } from './question-option-detail.component';

describe('Component Tests', () => {
  describe('QuestionOption Management Detail Component', () => {
    let comp: QuestionOptionDetailComponent;
    let fixture: ComponentFixture<QuestionOptionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [QuestionOptionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ questionOption: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(QuestionOptionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(QuestionOptionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load questionOption on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.questionOption).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
