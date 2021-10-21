import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FormAnswerComponent } from './list/form-answer.component';
import { FormAnswerDetailComponent } from './detail/form-answer-detail.component';
import { FormAnswerUpdateComponent } from './update/form-answer-update.component';
import { FormAnswerDeleteDialogComponent } from './delete/form-answer-delete-dialog.component';
import { FormAnswerRoutingModule } from './route/form-answer-routing.module';

@NgModule({
  imports: [SharedModule, FormAnswerRoutingModule],
  declarations: [FormAnswerComponent, FormAnswerDetailComponent, FormAnswerUpdateComponent, FormAnswerDeleteDialogComponent],
  entryComponents: [FormAnswerDeleteDialogComponent],
})
export class FormAnswerModule {}
