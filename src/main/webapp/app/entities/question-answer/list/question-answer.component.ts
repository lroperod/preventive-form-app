import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuestionAnswer } from '../question-answer.model';
import { QuestionAnswerService } from '../service/question-answer.service';
import { QuestionAnswerDeleteDialogComponent } from '../delete/question-answer-delete-dialog.component';

@Component({
  selector: 'pfa-question-answer',
  templateUrl: './question-answer.component.html',
})
export class QuestionAnswerComponent implements OnInit {
  questionAnswers?: IQuestionAnswer[];
  isLoading = false;

  constructor(protected questionAnswerService: QuestionAnswerService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.questionAnswerService.query().subscribe(
      (res: HttpResponse<IQuestionAnswer[]>) => {
        this.isLoading = false;
        this.questionAnswers = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IQuestionAnswer): number {
    return item.id!;
  }

  delete(questionAnswer: IQuestionAnswer): void {
    const modalRef = this.modalService.open(QuestionAnswerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.questionAnswer = questionAnswer;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
