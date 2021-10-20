import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuestionOption } from '../question-option.model';

@Component({
  selector: 'pfa-question-option-detail',
  templateUrl: './question-option-detail.component.html',
})
export class QuestionOptionDetailComponent implements OnInit {
  questionOption: IQuestionOption | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionOption }) => {
      this.questionOption = questionOption;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
