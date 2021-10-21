import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuestions, Questions } from '../questions.model';
import { QuestionsService } from '../service/questions.service';
import { IForm } from 'app/entities/form/form.model';
import { FormService } from 'app/entities/form/service/form.service';

@Component({
  selector: 'pfa-questions-update',
  templateUrl: './questions-update.component.html',
})
export class QuestionsUpdateComponent implements OnInit {
  isSaving = false;

  formsSharedCollection: IForm[] = [];

  editForm = this.fb.group({
    id: [],
    questionCode: [],
    questionText: [],
    questionType: [],
    form: [],
  });

  constructor(
    protected questionsService: QuestionsService,
    protected formService: FormService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questions }) => {
      this.updateForm(questions);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const questions = this.createFromForm();
    if (questions.id !== undefined) {
      this.subscribeToSaveResponse(this.questionsService.update(questions));
    } else {
      this.subscribeToSaveResponse(this.questionsService.create(questions));
    }
  }

  trackFormById(index: number, item: IForm): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestions>>): void {
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

  protected updateForm(questions: IQuestions): void {
    this.editForm.patchValue({
      id: questions.id,
      questionCode: questions.questionCode,
      questionText: questions.questionText,
      questionType: questions.questionType,
      form: questions.form,
    });

    this.formsSharedCollection = this.formService.addFormToCollectionIfMissing(this.formsSharedCollection, questions.form);
  }

  protected loadRelationshipsOptions(): void {
    this.formService
      .query()
      .pipe(map((res: HttpResponse<IForm[]>) => res.body ?? []))
      .pipe(map((forms: IForm[]) => this.formService.addFormToCollectionIfMissing(forms, this.editForm.get('form')!.value)))
      .subscribe((forms: IForm[]) => (this.formsSharedCollection = forms));
  }

  protected createFromForm(): IQuestions {
    return {
      ...new Questions(),
      id: this.editForm.get(['id'])!.value,
      questionCode: this.editForm.get(['questionCode'])!.value,
      questionText: this.editForm.get(['questionText'])!.value,
      questionType: this.editForm.get(['questionType'])!.value,
      form: this.editForm.get(['form'])!.value,
    };
  }
}
