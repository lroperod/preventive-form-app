import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuestionAnswer } from '../question-answer.model';
import { QuestionAnswerService } from '../service/question-answer.service';

@Component({
  templateUrl: './question-answer-delete-dialog.component.html',
})
export class QuestionAnswerDeleteDialogComponent {
  questionAnswer?: IQuestionAnswer;

  constructor(protected questionAnswerService: QuestionAnswerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.questionAnswerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
