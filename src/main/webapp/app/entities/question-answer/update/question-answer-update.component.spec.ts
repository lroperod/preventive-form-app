jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { QuestionAnswerService } from '../service/question-answer.service';
import { IQuestionAnswer, QuestionAnswer } from '../question-answer.model';
import { IFormAnswer } from 'app/entities/form-answer/form-answer.model';
import { FormAnswerService } from 'app/entities/form-answer/service/form-answer.service';

import { QuestionAnswerUpdateComponent } from './question-answer-update.component';

describe('Component Tests', () => {
  describe('QuestionAnswer Management Update Component', () => {
    let comp: QuestionAnswerUpdateComponent;
    let fixture: ComponentFixture<QuestionAnswerUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let questionAnswerService: QuestionAnswerService;
    let formAnswerService: FormAnswerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [QuestionAnswerUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(QuestionAnswerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuestionAnswerUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      questionAnswerService = TestBed.inject(QuestionAnswerService);
      formAnswerService = TestBed.inject(FormAnswerService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call FormAnswer query and add missing value', () => {
        const questionAnswer: IQuestionAnswer = { id: 456 };
        const formAnswer: IFormAnswer = { id: 31136 };
        questionAnswer.formAnswer = formAnswer;

        const formAnswerCollection: IFormAnswer[] = [{ id: 25253 }];
        jest.spyOn(formAnswerService, 'query').mockReturnValue(of(new HttpResponse({ body: formAnswerCollection })));
        const additionalFormAnswers = [formAnswer];
        const expectedCollection: IFormAnswer[] = [...additionalFormAnswers, ...formAnswerCollection];
        jest.spyOn(formAnswerService, 'addFormAnswerToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ questionAnswer });
        comp.ngOnInit();

        expect(formAnswerService.query).toHaveBeenCalled();
        expect(formAnswerService.addFormAnswerToCollectionIfMissing).toHaveBeenCalledWith(formAnswerCollection, ...additionalFormAnswers);
        expect(comp.formAnswersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const questionAnswer: IQuestionAnswer = { id: 456 };
        const formAnswer: IFormAnswer = { id: 50221 };
        questionAnswer.formAnswer = formAnswer;

        activatedRoute.data = of({ questionAnswer });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(questionAnswer));
        expect(comp.formAnswersSharedCollection).toContain(formAnswer);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<QuestionAnswer>>();
        const questionAnswer = { id: 123 };
        jest.spyOn(questionAnswerService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ questionAnswer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: questionAnswer }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(questionAnswerService.update).toHaveBeenCalledWith(questionAnswer);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<QuestionAnswer>>();
        const questionAnswer = new QuestionAnswer();
        jest.spyOn(questionAnswerService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ questionAnswer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: questionAnswer }));
        saveSubject.complete();

        // THEN
        expect(questionAnswerService.create).toHaveBeenCalledWith(questionAnswer);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<QuestionAnswer>>();
        const questionAnswer = { id: 123 };
        jest.spyOn(questionAnswerService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ questionAnswer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(questionAnswerService.update).toHaveBeenCalledWith(questionAnswer);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackFormAnswerById', () => {
        it('Should return tracked FormAnswer primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFormAnswerById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
