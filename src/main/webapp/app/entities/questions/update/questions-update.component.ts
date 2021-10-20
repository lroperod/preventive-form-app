import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuestions, Questions } from '../questions.model';
import { QuestionsService } from '../service/questions.service';
import { IQuestionAnswer } from 'app/entities/question-answer/question-answer.model';
import { QuestionAnswerService } from 'app/entities/question-answer/service/question-answer.service';
import { IForm } from 'app/entities/form/form.model';
import { FormService } from 'app/entities/form/service/form.service';
import { IQuestionOption } from 'app/entities/question-option/question-option.model';
import { QuestionOptionService } from 'app/entities/question-option/service/question-option.service';

@Component({
  selector: 'pfa-questions-update',
  templateUrl: './questions-update.component.html',
})
export class QuestionsUpdateComponent implements OnInit {
  isSaving = false;

  questionsCollection: IQuestionAnswer[] = [];
  formsSharedCollection: IForm[] = [];
  questionOptionsSharedCollection: IQuestionOption[] = [];

  editForm = this.fb.group({
    id: [],
    questionCode: [],
    questionText: [],
    questionType: [],
    question: [],
    form: [],
    questionOption: [],
  });

  constructor(
    protected questionsService: QuestionsService,
    protected questionAnswerService: QuestionAnswerService,
    protected formService: FormService,
    protected questionOptionService: QuestionOptionService,
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

  trackQuestionAnswerById(index: number, item: IQuestionAnswer): number {
    return item.id!;
  }

  trackFormById(index: number, item: IForm): number {
    return item.id!;
  }

  trackQuestionOptionById(index: number, item: IQuestionOption): number {
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
      question: questions.question,
      form: questions.form,
      questionOption: questions.questionOption,
    });

    this.questionsCollection = this.questionAnswerService.addQuestionAnswerToCollectionIfMissing(
      this.questionsCollection,
      questions.question
    );
    this.formsSharedCollection = this.formService.addFormToCollectionIfMissing(this.formsSharedCollection, questions.form);
    this.questionOptionsSharedCollection = this.questionOptionService.addQuestionOptionToCollectionIfMissing(
      this.questionOptionsSharedCollection,
      questions.questionOption
    );
  }

  protected loadRelationshipsOptions(): void {
    this.questionAnswerService
      .query({ filter: 'questions-is-null' })
      .pipe(map((res: HttpResponse<IQuestionAnswer[]>) => res.body ?? []))
      .pipe(
        map((questionAnswers: IQuestionAnswer[]) =>
          this.questionAnswerService.addQuestionAnswerToCollectionIfMissing(questionAnswers, this.editForm.get('question')!.value)
        )
      )
      .subscribe((questionAnswers: IQuestionAnswer[]) => (this.questionsCollection = questionAnswers));

    this.formService
      .query()
      .pipe(map((res: HttpResponse<IForm[]>) => res.body ?? []))
      .pipe(map((forms: IForm[]) => this.formService.addFormToCollectionIfMissing(forms, this.editForm.get('form')!.value)))
      .subscribe((forms: IForm[]) => (this.formsSharedCollection = forms));

    this.questionOptionService
      .query()
      .pipe(map((res: HttpResponse<IQuestionOption[]>) => res.body ?? []))
      .pipe(
        map((questionOptions: IQuestionOption[]) =>
          this.questionOptionService.addQuestionOptionToCollectionIfMissing(questionOptions, this.editForm.get('questionOption')!.value)
        )
      )
      .subscribe((questionOptions: IQuestionOption[]) => (this.questionOptionsSharedCollection = questionOptions));
  }

  protected createFromForm(): IQuestions {
    return {
      ...new Questions(),
      id: this.editForm.get(['id'])!.value,
      questionCode: this.editForm.get(['questionCode'])!.value,
      questionText: this.editForm.get(['questionText'])!.value,
      questionType: this.editForm.get(['questionType'])!.value,
      question: this.editForm.get(['question'])!.value,
      form: this.editForm.get(['form'])!.value,
      questionOption: this.editForm.get(['questionOption'])!.value,
    };
  }
}
