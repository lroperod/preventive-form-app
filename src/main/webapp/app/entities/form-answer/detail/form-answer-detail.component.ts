import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFormAnswer } from '../form-answer.model';

@Component({
  selector: 'pfa-form-answer-detail',
  templateUrl: './form-answer-detail.component.html',
})
export class FormAnswerDetailComponent implements OnInit {
  formAnswer: IFormAnswer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formAnswer }) => {
      this.formAnswer = formAnswer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
