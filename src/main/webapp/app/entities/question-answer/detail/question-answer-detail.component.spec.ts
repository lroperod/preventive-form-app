import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { QuestionAnswerDetailComponent } from './question-answer-detail.component';

describe('Component Tests', () => {
  describe('QuestionAnswer Management Detail Component', () => {
    let comp: QuestionAnswerDetailComponent;
    let fixture: ComponentFixture<QuestionAnswerDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [QuestionAnswerDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ questionAnswer: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(QuestionAnswerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(QuestionAnswerDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load questionAnswer on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.questionAnswer).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
