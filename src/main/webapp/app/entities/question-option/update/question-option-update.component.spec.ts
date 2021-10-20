jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { QuestionOptionService } from '../service/question-option.service';
import { IQuestionOption, QuestionOption } from '../question-option.model';
import { IQuestions } from 'app/entities/questions/questions.model';
import { QuestionsService } from 'app/entities/questions/service/questions.service';

import { QuestionOptionUpdateComponent } from './question-option-update.component';

describe('Component Tests', () => {
  describe('QuestionOption Management Update Component', () => {
    let comp: QuestionOptionUpdateComponent;
    let fixture: ComponentFixture<QuestionOptionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let questionOptionService: QuestionOptionService;
    let questionsService: QuestionsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [QuestionOptionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(QuestionOptionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuestionOptionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      questionOptionService = TestBed.inject(QuestionOptionService);
      questionsService = TestBed.inject(QuestionsService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Questions query and add missing value', () => {
        const questionOption: IQuestionOption = { id: 456 };
        const questions: IQuestions = { id: 83794 };
        questionOption.questions = questions;

        const questionsCollection: IQuestions[] = [{ id: 46285 }];
        jest.spyOn(questionsService, 'query').mockReturnValue(of(new HttpResponse({ body: questionsCollection })));
        const additionalQuestions = [questions];
        const expectedCollection: IQuestions[] = [...additionalQuestions, ...questionsCollection];
        jest.spyOn(questionsService, 'addQuestionsToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ questionOption });
        comp.ngOnInit();

        expect(questionsService.query).toHaveBeenCalled();
        expect(questionsService.addQuestionsToCollectionIfMissing).toHaveBeenCalledWith(questionsCollection, ...additionalQuestions);
        expect(comp.questionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const questionOption: IQuestionOption = { id: 456 };
        const questions: IQuestions = { id: 59573 };
        questionOption.questions = questions;

        activatedRoute.data = of({ questionOption });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(questionOption));
        expect(comp.questionsSharedCollection).toContain(questions);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<QuestionOption>>();
        const questionOption = { id: 123 };
        jest.spyOn(questionOptionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ questionOption });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: questionOption }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(questionOptionService.update).toHaveBeenCalledWith(questionOption);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<QuestionOption>>();
        const questionOption = new QuestionOption();
        jest.spyOn(questionOptionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ questionOption });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: questionOption }));
        saveSubject.complete();

        // THEN
        expect(questionOptionService.create).toHaveBeenCalledWith(questionOption);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<QuestionOption>>();
        const questionOption = { id: 123 };
        jest.spyOn(questionOptionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ questionOption });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(questionOptionService.update).toHaveBeenCalledWith(questionOption);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackQuestionsById', () => {
        it('Should return tracked Questions primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackQuestionsById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
