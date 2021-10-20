import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFormAnswer } from '../form-answer.model';
import { FormAnswerService } from '../service/form-answer.service';

@Component({
  templateUrl: './form-answer-delete-dialog.component.html',
})
export class FormAnswerDeleteDialogComponent {
  formAnswer?: IFormAnswer;

  constructor(protected formAnswerService: FormAnswerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formAnswerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
