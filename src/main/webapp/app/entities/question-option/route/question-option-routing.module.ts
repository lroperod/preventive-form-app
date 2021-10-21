import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { QuestionOptionComponent } from '../list/question-option.component';
import { QuestionOptionDetailComponent } from '../detail/question-option-detail.component';
import { QuestionOptionUpdateComponent } from '../update/question-option-update.component';
import { QuestionOptionRoutingResolveService } from './question-option-routing-resolve.service';

const questionOptionRoute: Routes = [
  {
    path: '',
    component: QuestionOptionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuestionOptionDetailComponent,
    resolve: {
      questionOption: QuestionOptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuestionOptionUpdateComponent,
    resolve: {
      questionOption: QuestionOptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuestionOptionUpdateComponent,
    resolve: {
      questionOption: QuestionOptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(questionOptionRoute)],
  exports: [RouterModule],
})
export class QuestionOptionRoutingModule {}
