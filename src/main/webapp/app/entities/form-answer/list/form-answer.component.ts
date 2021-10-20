import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFormAnswer } from '../form-answer.model';
import { FormAnswerService } from '../service/form-answer.service';
import { FormAnswerDeleteDialogComponent } from '../delete/form-answer-delete-dialog.component';

@Component({
  selector: 'pfa-form-answer',
  templateUrl: './form-answer.component.html',
})
export class FormAnswerComponent implements OnInit {
  formAnswers?: IFormAnswer[];
  isLoading = false;

  constructor(protected formAnswerService: FormAnswerService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.formAnswerService.query().subscribe(
      (res: HttpResponse<IFormAnswer[]>) => {
        this.isLoading = false;
        this.formAnswers = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFormAnswer): number {
    return item.id!;
  }

  delete(formAnswer: IFormAnswer): void {
    const modalRef = this.modalService.open(FormAnswerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.formAnswer = formAnswer;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
