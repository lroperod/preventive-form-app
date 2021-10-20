import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FormAnswerComponent } from '../list/form-answer.component';
import { FormAnswerDetailComponent } from '../detail/form-answer-detail.component';
import { FormAnswerUpdateComponent } from '../update/form-answer-update.component';
import { FormAnswerRoutingResolveService } from './form-answer-routing-resolve.service';

const formAnswerRoute: Routes = [
  {
    path: '',
    component: FormAnswerComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormAnswerDetailComponent,
    resolve: {
      formAnswer: FormAnswerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormAnswerUpdateComponent,
    resolve: {
      formAnswer: FormAnswerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormAnswerUpdateComponent,
    resolve: {
      formAnswer: FormAnswerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(formAnswerRoute)],
  exports: [RouterModule],
})
export class FormAnswerRoutingModule {}
