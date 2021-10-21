import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'form',
        data: { pageTitle: 'preventiveformappApp.form.home.title' },
        loadChildren: () => import('./form/form.module').then(m => m.FormModule),
      },
      {
        path: 'questions',
        data: { pageTitle: 'preventiveformappApp.questions.home.title' },
        loadChildren: () => import('./questions/questions.module').then(m => m.QuestionsModule),
      },
      {
        path: 'question-option',
        data: { pageTitle: 'preventiveformappApp.questionOption.home.title' },
        loadChildren: () => import('./question-option/question-option.module').then(m => m.QuestionOptionModule),
      },
      {
        path: 'question-answer',
        data: { pageTitle: 'preventiveformappApp.questionAnswer.home.title' },
        loadChildren: () => import('./question-answer/question-answer.module').then(m => m.QuestionAnswerModule),
      },
      {
        path: 'form-answer',
        data: { pageTitle: 'preventiveformappApp.formAnswer.home.title' },
        loadChildren: () => import('./form-answer/form-answer.module').then(m => m.FormAnswerModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
