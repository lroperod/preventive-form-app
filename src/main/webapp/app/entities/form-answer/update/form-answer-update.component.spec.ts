jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FormAnswerService } from '../service/form-answer.service';
import { IFormAnswer, FormAnswer } from '../form-answer.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IForm } from 'app/entities/form/form.model';
import { FormService } from 'app/entities/form/service/form.service';

import { FormAnswerUpdateComponent } from './form-answer-update.component';

describe('Component Tests', () => {
  describe('FormAnswer Management Update Component', () => {
    let comp: FormAnswerUpdateComponent;
    let fixture: ComponentFixture<FormAnswerUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let formAnswerService: FormAnswerService;
    let userService: UserService;
    let formService: FormService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FormAnswerUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FormAnswerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FormAnswerUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      formAnswerService = TestBed.inject(FormAnswerService);
      userService = TestBed.inject(UserService);
      formService = TestBed.inject(FormService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const formAnswer: IFormAnswer = { id: 456 };
        const users: IUser[] = [{ id: 39681 }];
        formAnswer.users = users;

        const userCollection: IUser[] = [{ id: 46096 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [...users];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ formAnswer });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Form query and add missing value', () => {
        const formAnswer: IFormAnswer = { id: 456 };
        const form: IForm = { id: 52958 };
        formAnswer.form = form;

        const formCollection: IForm[] = [{ id: 58835 }];
        jest.spyOn(formService, 'query').mockReturnValue(of(new HttpResponse({ body: formCollection })));
        const additionalForms = [form];
        const expectedCollection: IForm[] = [...additionalForms, ...formCollection];
        jest.spyOn(formService, 'addFormToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ formAnswer });
        comp.ngOnInit();

        expect(formService.query).toHaveBeenCalled();
        expect(formService.addFormToCollectionIfMissing).toHaveBeenCalledWith(formCollection, ...additionalForms);
        expect(comp.formsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const formAnswer: IFormAnswer = { id: 456 };
        const users: IUser = { id: 64796 };
        formAnswer.users = [users];
        const form: IForm = { id: 79211 };
        formAnswer.form = form;

        activatedRoute.data = of({ formAnswer });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(formAnswer));
        expect(comp.usersSharedCollection).toContain(users);
        expect(comp.formsSharedCollection).toContain(form);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FormAnswer>>();
        const formAnswer = { id: 123 };
        jest.spyOn(formAnswerService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ formAnswer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: formAnswer }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(formAnswerService.update).toHaveBeenCalledWith(formAnswer);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FormAnswer>>();
        const formAnswer = new FormAnswer();
        jest.spyOn(formAnswerService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ formAnswer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: formAnswer }));
        saveSubject.complete();

        // THEN
        expect(formAnswerService.create).toHaveBeenCalledWith(formAnswer);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FormAnswer>>();
        const formAnswer = { id: 123 };
        jest.spyOn(formAnswerService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ formAnswer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(formAnswerService.update).toHaveBeenCalledWith(formAnswer);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
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
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedUser', () => {
        it('Should return option if no User is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedUser(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected User for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedUser(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this User is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedUser(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
