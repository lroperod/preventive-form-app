import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFormAnswer, FormAnswer } from '../form-answer.model';
import { FormAnswerService } from '../service/form-answer.service';

@Injectable({ providedIn: 'root' })
export class FormAnswerRoutingResolveService implements Resolve<IFormAnswer> {
  constructor(protected service: FormAnswerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFormAnswer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((formAnswer: HttpResponse<FormAnswer>) => {
          if (formAnswer.body) {
            return of(formAnswer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FormAnswer());
  }
}
