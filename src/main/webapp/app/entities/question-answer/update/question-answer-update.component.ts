import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuestionAnswer, QuestionAnswer } from '../question-answer.model';
import { QuestionAnswerService } from '../service/question-answer.service';
import { IFormAnswer } from 'app/entities/form-answer/form-answer.model';
import { FormAnswerService } from 'app/entities/form-answer/service/form-answer.service';

@Component({
  selector: 'pfa-question-answer-update',
  templateUrl: './question-answer-update.component.html',
})
export class QuestionAnswerUpdateComponent implements OnInit {
  isSaving = false;

  formAnswersSharedCollection: IFormAnswer[] = [];

  editForm = this.fb.group({
    id: [],
    answerCode: [],
    answerText: [],
    formAnswer: [],
  });

  constructor(
    protected questionAnswerService: QuestionAnswerService,
    protected formAnswerService: FormAnswerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionAnswer }) => {
      this.updateForm(questionAnswer);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const questionAnswer = this.createFromForm();
    if (questionAnswer.id !== undefined) {
      this.subscribeToSaveResponse(this.questionAnswerService.update(questionAnswer));
    } else {
      this.subscribeToSaveResponse(this.questionAnswerService.create(questionAnswer));
    }
  }

  trackFormAnswerById(index: number, item: IFormAnswer): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestionAnswer>>): void {
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

  protected updateForm(questionAnswer: IQuestionAnswer): void {
    this.editForm.patchValue({
      id: questionAnswer.id,
      answerCode: questionAnswer.answerCode,
      answerText: questionAnswer.answerText,
      formAnswer: questionAnswer.formAnswer,
    });

    this.formAnswersSharedCollection = this.formAnswerService.addFormAnswerToCollectionIfMissing(
      this.formAnswersSharedCollection,
      questionAnswer.formAnswer
    );
  }

  protected loadRelationshipsOptions(): void {
    this.formAnswerService
      .query()
      .pipe(map((res: HttpResponse<IFormAnswer[]>) => res.body ?? []))
      .pipe(
        map((formAnswers: IFormAnswer[]) =>
          this.formAnswerService.addFormAnswerToCollectionIfMissing(formAnswers, this.editForm.get('formAnswer')!.value)
        )
      )
      .subscribe((formAnswers: IFormAnswer[]) => (this.formAnswersSharedCollection = formAnswers));
  }

  protected createFromForm(): IQuestionAnswer {
    return {
      ...new QuestionAnswer(),
      id: this.editForm.get(['id'])!.value,
      answerCode: this.editForm.get(['answerCode'])!.value,
      answerText: this.editForm.get(['answerText'])!.value,
      formAnswer: this.editForm.get(['formAnswer'])!.value,
    };
  }
}
