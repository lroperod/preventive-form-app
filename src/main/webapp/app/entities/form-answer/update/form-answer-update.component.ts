import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFormAnswer, FormAnswer } from '../form-answer.model';
import { FormAnswerService } from '../service/form-answer.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IForm } from 'app/entities/form/form.model';
import { FormService } from 'app/entities/form/service/form.service';

@Component({
  selector: 'pfa-form-answer-update',
  templateUrl: './form-answer-update.component.html',
})
export class FormAnswerUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  formsSharedCollection: IForm[] = [];

  editForm = this.fb.group({
    id: [],
    formAnswerLocalDate: [],
    users: [],
    form: [],
  });

  constructor(
    protected formAnswerService: FormAnswerService,
    protected userService: UserService,
    protected formService: FormService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formAnswer }) => {
      this.updateForm(formAnswer);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const formAnswer = this.createFromForm();
    if (formAnswer.id !== undefined) {
      this.subscribeToSaveResponse(this.formAnswerService.update(formAnswer));
    } else {
      this.subscribeToSaveResponse(this.formAnswerService.create(formAnswer));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackFormById(index: number, item: IForm): number {
    return item.id!;
  }

  getSelectedUser(option: IUser, selectedVals?: IUser[]): IUser {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormAnswer>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(formAnswer: IFormAnswer): void {
    this.editForm.patchValue({
      id: formAnswer.id,
      formAnswerLocalDate: formAnswer.formAnswerLocalDate,
      users: formAnswer.users,
      form: formAnswer.form,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, ...(formAnswer.users ?? []));
    this.formsSharedCollection = this.formService.addFormToCollectionIfMissing(this.formsSharedCollection, formAnswer.form);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, ...(this.editForm.get('users')!.value ?? []))))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.formService
      .query()
      .pipe(map((res: HttpResponse<IForm[]>) => res.body ?? []))
      .pipe(map((forms: IForm[]) => this.formService.addFormToCollectionIfMissing(forms, this.editForm.get('form')!.value)))
      .subscribe((forms: IForm[]) => (this.formsSharedCollection = forms));
  }

  protected createFromForm(): IFormAnswer {
    return {
      ...new FormAnswer(),
      id: this.editForm.get(['id'])!.value,
      formAnswerLocalDate: this.editForm.get(['formAnswerLocalDate'])!.value,
      users: this.editForm.get(['users'])!.value,
      form: this.editForm.get(['form'])!.value,
    };
  }
}
