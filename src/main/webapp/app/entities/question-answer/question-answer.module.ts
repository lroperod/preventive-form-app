import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { QuestionAnswerComponent } from './list/question-answer.component';
import { QuestionAnswerDetailComponent } from './detail/question-answer-detail.component';
import { QuestionAnswerUpdateComponent } from './update/question-answer-update.component';
import { QuestionAnswerDeleteDialogComponent } from './delete/question-answer-delete-dialog.component';
import { QuestionAnswerRoutingModule } from './route/question-answer-routing.module';

@NgModule({
  imports: [SharedModule, QuestionAnswerRoutingModule],
  declarations: [
    QuestionAnswerComponent,
    QuestionAnswerDetailComponent,
    QuestionAnswerUpdateComponent,
    QuestionAnswerDeleteDialogComponent,
  ],
  entryComponents: [QuestionAnswerDeleteDialogComponent],
})
export class QuestionAnswerModule {}
