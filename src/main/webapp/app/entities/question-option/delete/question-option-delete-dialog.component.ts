import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuestionOption } from '../question-option.model';
import { QuestionOptionService } from '../service/question-option.service';

@Component({
  templateUrl: './question-option-delete-dialog.component.html',
})
export class QuestionOptionDeleteDialogComponent {
  questionOption?: IQuestionOption;

  constructor(protected questionOptionService: QuestionOptionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.questionOptionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
