import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuestionAnswer } from '../question-answer.model';

@Component({
  selector: 'pfa-question-answer-detail',
  templateUrl: './question-answer-detail.component.html',
})
export class QuestionAnswerDetailComponent implements OnInit {
  questionAnswer: IQuestionAnswer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionAnswer }) => {
      this.questionAnswer = questionAnswer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
