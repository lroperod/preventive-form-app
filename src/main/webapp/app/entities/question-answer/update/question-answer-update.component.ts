import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuestionAnswer, QuestionAnswer } from '../question-answer.model';
import { QuestionAnswerService } from '../service/question-answer.service';
import { IQuestions } from 'app/entities/questions/questions.model';
import { QuestionsService } from 'app/entities/questions/service/questions.service';
import { IFormAnswer } from 'app/entities/form-answer/form-answer.model';
import { FormAnswerService } from 'app/entities/form-answer/service/form-answer.service';

@Component({
  selector: 'pfa-question-answer-update',
  templateUrl: './question-answer-update.component.html',
})
export class QuestionAnswerUpdateComponent implements OnInit {
  isSaving = false;

  questionsCollection: IQuestions[] = [];
  formAnswersSharedCollection: IFormAnswer[] = [];

  editForm = this.fb.group({
    id: [],
    answerCode: [],
    answerText: [],
    questions: [],
    formAnswer: [],
  });

  constructor(
    protected questionAnswerService: QuestionAnswerService,
    protected questionsService: QuestionsService,
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

  trackQuestionsById(index: number, item: IQuestions): number {
    return item.id!;
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
      questions: questionAnswer.questions,
      formAnswer: questionAnswer.formAnswer,
    });

    this.questionsCollection = this.questionsService.addQuestionsToCollectionIfMissing(this.questionsCollection, questionAnswer.questions);
    this.formAnswersSharedCollection = this.formAnswerService.addFormAnswerToCollectionIfMissing(
      this.formAnswersSharedCollection,
      questionAnswer.formAnswer
    );
  }

  protected loadRelationshipsOptions(): void {
    this.questionsService
      .query({ filter: 'questionanswer-is-null' })
      .pipe(map((res: HttpResponse<IQuestions[]>) => res.body ?? []))
      .pipe(
        map((questions: IQuestions[]) =>
          this.questionsService.addQuestionsToCollectionIfMissing(questions, this.editForm.get('questions')!.value)
        )
      )
      .subscribe((questions: IQuestions[]) => (this.questionsCollection = questions));

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
      questions: this.editForm.get(['questions'])!.value,
      formAnswer: this.editForm.get(['formAnswer'])!.value,
    };
  }
}
