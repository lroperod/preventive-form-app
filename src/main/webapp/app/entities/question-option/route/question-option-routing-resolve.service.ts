import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuestionOption, QuestionOption } from '../question-option.model';
import { QuestionOptionService } from '../service/question-option.service';

@Injectable({ providedIn: 'root' })
export class QuestionOptionRoutingResolveService implements Resolve<IQuestionOption> {
  constructor(protected service: QuestionOptionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQuestionOption> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((questionOption: HttpResponse<QuestionOption>) => {
          if (questionOption.body) {
            return of(questionOption.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new QuestionOption());
  }
}
