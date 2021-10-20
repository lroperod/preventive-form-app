import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuestionAnswer, QuestionAnswer } from '../question-answer.model';
import { QuestionAnswerService } from '../service/question-answer.service';

@Injectable({ providedIn: 'root' })
export class QuestionAnswerRoutingResolveService implements Resolve<IQuestionAnswer> {
  constructor(protected service: QuestionAnswerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQuestionAnswer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((questionAnswer: HttpResponse<QuestionAnswer>) => {
          if (questionAnswer.body) {
            return of(questionAnswer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new QuestionAnswer());
  }
}
