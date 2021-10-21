import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { QuestionOptionComponent } from './list/question-option.component';
import { QuestionOptionDetailComponent } from './detail/question-option-detail.component';
import { QuestionOptionUpdateComponent } from './update/question-option-update.component';
import { QuestionOptionDeleteDialogComponent } from './delete/question-option-delete-dialog.component';
import { QuestionOptionRoutingModule } from './route/question-option-routing.module';

@NgModule({
  imports: [SharedModule, QuestionOptionRoutingModule],
  declarations: [
    QuestionOptionComponent,
    QuestionOptionDetailComponent,
    QuestionOptionUpdateComponent,
    QuestionOptionDeleteDialogComponent,
  ],
  entryComponents: [QuestionOptionDeleteDialogComponent],
})
export class QuestionOptionModule {}
