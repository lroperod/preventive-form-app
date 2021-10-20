import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuestionOption } from '../question-option.model';
import { QuestionOptionService } from '../service/question-option.service';
import { QuestionOptionDeleteDialogComponent } from '../delete/question-option-delete-dialog.component';

@Component({
  selector: 'pfa-question-option',
  templateUrl: './question-option.component.html',
})
export class QuestionOptionComponent implements OnInit {
  questionOptions?: IQuestionOption[];
  isLoading = false;

  constructor(protected questionOptionService: QuestionOptionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.questionOptionService.query().subscribe(
      (res: HttpResponse<IQuestionOption[]>) => {
        this.isLoading = false;
        this.questionOptions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IQuestionOption): number {
    return item.id!;
  }

  delete(questionOption: IQuestionOption): void {
    const modalRef = this.modalService.open(QuestionOptionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.questionOption = questionOption;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
