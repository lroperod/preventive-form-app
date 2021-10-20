import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IQuestionOption, QuestionOption } from '../question-option.model';
import { QuestionOptionService } from '../service/question-option.service';

@Component({
  selector: 'pfa-question-option-update',
  templateUrl: './question-option-update.component.html',
})
export class QuestionOptionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    questionOptionsCode: [],
    questionOptionsText: [],
  });

  constructor(
    protected questionOptionService: QuestionOptionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionOption }) => {
      this.updateForm(questionOption);
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
    });
  }

  protected createFromForm(): IQuestionOption {
    return {
      ...new QuestionOption(),
      id: this.editForm.get(['id'])!.value,
      questionOptionsCode: this.editForm.get(['questionOptionsCode'])!.value,
      questionOptionsText: this.editForm.get(['questionOptionsText'])!.value,
    };
  }
}
