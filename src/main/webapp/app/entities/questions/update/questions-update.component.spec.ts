jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { QuestionsService } from '../service/questions.service';
import { IQuestions, Questions } from '../questions.model';
import { IQuestionAnswer } from 'app/entities/question-answer/question-answer.model';
import { QuestionAnswerService } from 'app/entities/question-answer/service/question-answer.service';
import { IForm } from 'app/entities/form/form.model';
import { FormService } from 'app/entities/form/service/form.service';
import { IQuestionOption } from 'app/entities/question-option/question-option.model';
import { QuestionOptionService } from 'app/entities/question-option/service/question-option.service';

import { QuestionsUpdateComponent } from './questions-update.component';

describe('Component Tests', () => {
  describe('Questions Management Update Component', () => {
    let comp: QuestionsUpdateComponent;
    let fixture: ComponentFixture<QuestionsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let questionsService: QuestionsService;
    let questionAnswerService: QuestionAnswerService;
    let formService: FormService;
    let questionOptionService: QuestionOptionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [QuestionsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(QuestionsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuestionsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      questionsService = TestBed.inject(QuestionsService);
      questionAnswerService = TestBed.inject(QuestionAnswerService);
      formService = TestBed.inject(FormService);
      questionOptionService = TestBed.inject(QuestionOptionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call question query and add missing value', () => {
        const questions: IQuestions = { id: 456 };
        const question: IQuestionAnswer = { id: 65103 };
        questions.question = question;

        const questionCollection: IQuestionAnswer[] = [{ id: 85758 }];
        jest.spyOn(questionAnswerService, 'query').mockReturnValue(of(new HttpResponse({ body: questionCollection })));
        const expectedCollection: IQuestionAnswer[] = [question, ...questionCollection];
        jest.spyOn(questionAnswerService, 'addQuestionAnswerToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ questions });
        comp.ngOnInit();

        expect(questionAnswerService.query).toHaveBeenCalled();
        expect(questionAnswerService.addQuestionAnswerToCollectionIfMissing).toHaveBeenCalledWith(questionCollection, question);
        expect(comp.questionsCollection).toEqual(expectedCollection);
      });

      it('Should call Form query and add missing value', () => {
        const questions: IQuestions = { id: 456 };
        const form: IForm = { id: 96076 };
        questions.form = form;

        const formCollection: IForm[] = [{ id: 12716 }];
        jest.spyOn(formService, 'query').mockReturnValue(of(new HttpResponse({ body: formCollection })));
        const additionalForms = [form];
        const expectedCollection: IForm[] = [...additionalForms, ...formCollection];
        jest.spyOn(formService, 'addFormToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ questions });
        comp.ngOnInit();

        expect(formService.query).toHaveBeenCalled();
        expect(formService.addFormToCollectionIfMissing).toHaveBeenCalledWith(formCollection, ...additionalForms);
        expect(comp.formsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call QuestionOption query and add missing value', () => {
        const questions: IQuestions = { id: 456 };
        const questionOption: IQuestionOption = { id: 74821 };
        questions.questionOption = questionOption;

        const questionOptionCollection: IQuestionOption[] = [{ id: 93778 }];
        jest.spyOn(questionOptionService, 'query').mockReturnValue(of(new HttpResponse({ body: questionOptionCollection })));
        const additionalQuestionOptions = [questionOption];
        const expectedCollection: IQuestionOption[] = [...additionalQuestionOptions, ...questionOptionCollection];
        jest.spyOn(questionOptionService, 'addQuestionOptionToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ questions });
        comp.ngOnInit();

        expect(questionOptionService.query).toHaveBeenCalled();
        expect(questionOptionService.addQuestionOptionToCollectionIfMissing).toHaveBeenCalledWith(
          questionOptionCollection,
          ...additionalQuestionOptions
        );
        expect(comp.questionOptionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const questions: IQuestions = { id: 456 };
        const question: IQuestionAnswer = { id: 10574 };
        questions.question = question;
        const form: IForm = { id: 8588 };
        questions.form = form;
        const questionOption: IQuestionOption = { id: 32950 };
        questions.questionOption = questionOption;

        activatedRoute.data = of({ questions });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(questions));
        expect(comp.questionsCollection).toContain(question);
        expect(comp.formsSharedCollection).toContain(form);
        expect(comp.questionOptionsSharedCollection).toContain(questionOption);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Questions>>();
        const questions = { id: 123 };
        jest.spyOn(questionsService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ questions });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: questions }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(questionsService.update).toHaveBeenCalledWith(questions);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Questions>>();
        const questions = new Questions();
        jest.spyOn(questionsService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ questions });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: questions }));
        saveSubject.complete();

        // THEN
        expect(questionsService.create).toHaveBeenCalledWith(questions);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Questions>>();
        const questions = { id: 123 };
        jest.spyOn(questionsService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ questions });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(questionsService.update).toHaveBeenCalledWith(questions);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackQuestionAnswerById', () => {
        it('Should return tracked QuestionAnswer primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackQuestionAnswerById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackFormById', () => {
        it('Should return tracked Form primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFormById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackQuestionOptionById', () => {
        it('Should return tracked QuestionOption primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackQuestionOptionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
