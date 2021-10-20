jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { QuestionsService } from '../service/questions.service';
import { IQuestions, Questions } from '../questions.model';
import { IForm } from 'app/entities/form/form.model';
import { FormService } from 'app/entities/form/service/form.service';

import { QuestionsUpdateComponent } from './questions-update.component';

describe('Component Tests', () => {
  describe('Questions Management Update Component', () => {
    let comp: QuestionsUpdateComponent;
    let fixture: ComponentFixture<QuestionsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let questionsService: QuestionsService;
    let formService: FormService;

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
      formService = TestBed.inject(FormService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
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

      it('Should update editForm', () => {
        const questions: IQuestions = { id: 456 };
        const form: IForm = { id: 8588 };
        questions.form = form;

        activatedRoute.data = of({ questions });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(questions));
        expect(comp.formsSharedCollection).toContain(form);
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
      describe('trackFormById', () => {
        it('Should return tracked Form primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFormById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
