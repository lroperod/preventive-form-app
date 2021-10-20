import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuestionOption, QuestionOption } from '../question-option.model';
import { QuestionOptionService } from '../service/question-option.service';
import { IQuestions } from 'app/entities/questions/questions.model';
import { QuestionsService } from 'app/entities/questions/service/questions.service';

@Component({
  selector: 'pfa-question-option-update',
  templateUrl: './question-option-update.component.html',
})
export class QuestionOptionUpdateComponent implements OnInit {
  isSaving = false;

  questionsSharedCollection: IQuestions[] = [];

  editForm = this.fb.group({
    id: [],
    questionOptionsCode: [],
    questionOptionsText: [],
    questions: [],
  });

  constructor(
    protected questionOptionService: QuestionOptionService,
    protected questionsService: QuestionsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionOption }) => {
      this.updateForm(questionOption);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const questionOption = this.createFromForm();
    if (questionOption.id !== undefined) {
      this.subscribeToSaveResponse(this.questionOptionService.update(questionOption));
    } else {
      this.subscribeToSaveResponse(this.questionOptionService.create(questionOption));
    }
  }

  trackQuestionsById(index: number, item: IQuestions): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestionOption>>): void {
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

  protected updateForm(questionOption: IQuestionOption): void {
    this.editForm.patchValue({
      id: questionOption.id,
      questionOptionsCode: questionOption.questionOptionsCode,
      questionOptionsText: questionOption.questionOptionsText,
      questions: questionOption.questions,
    });

    this.questionsSharedCollection = this.questionsService.addQuestionsToCollectionIfMissing(
      this.questionsSharedCollection,
      questionOption.questions
    );
  }

  protected loadRelationshipsOptions(): void {
    this.questionsService
      .query()
      .pipe(map((res: HttpResponse<IQuestions[]>) => res.body ?? []))
      .pipe(
        map((questions: IQuestions[]) =>
          this.questionsService.addQuestionsToCollectionIfMissing(questions, this.editForm.get('questions')!.value)
        )
      )
      .subscribe((questions: IQuestions[]) => (this.questionsSharedCollection = questions));
  }

  protected createFromForm(): IQuestionOption {
    return {
      ...new QuestionOption(),
      id: this.editForm.get(['id'])!.value,
      questionOptionsCode: this.editForm.get(['questionOptionsCode'])!.value,
      questionOptionsText: this.editForm.get(['questionOptionsText'])!.value,
      questions: this.editForm.get(['questions'])!.value,
    };
  }
}
