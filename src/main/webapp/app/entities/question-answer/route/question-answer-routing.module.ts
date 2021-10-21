import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { QuestionAnswerComponent } from '../list/question-answer.component';
import { QuestionAnswerDetailComponent } from '../detail/question-answer-detail.component';
import { QuestionAnswerUpdateComponent } from '../update/question-answer-update.component';
import { QuestionAnswerRoutingResolveService } from './question-answer-routing-resolve.service';

const questionAnswerRoute: Routes = [
  {
    path: '',
    component: QuestionAnswerComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuestionAnswerDetailComponent,
    resolve: {
      questionAnswer: QuestionAnswerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuestionAnswerUpdateComponent,
    resolve: {
      questionAnswer: QuestionAnswerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuestionAnswerUpdateComponent,
    resolve: {
      questionAnswer: QuestionAnswerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(questionAnswerRoute)],
  exports: [RouterModule],
})
export class QuestionAnswerRoutingModule {}
