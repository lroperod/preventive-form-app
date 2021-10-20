import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFormAnswer, getFormAnswerIdentifier } from '../form-answer.model';

export type EntityResponseType = HttpResponse<IFormAnswer>;
export type EntityArrayResponseType = HttpResponse<IFormAnswer[]>;

@Injectable({ providedIn: 'root' })
export class FormAnswerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/form-answers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(formAnswer: IFormAnswer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formAnswer);
    return this.http
      .post<IFormAnswer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(formAnswer: IFormAnswer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formAnswer);
    return this.http
      .put<IFormAnswer>(`${this.resourceUrl}/${getFormAnswerIdentifier(formAnswer) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(formAnswer: IFormAnswer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formAnswer);
    return this.http
      .patch<IFormAnswer>(`${this.resourceUrl}/${getFormAnswerIdentifier(formAnswer) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFormAnswer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFormAnswer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFormAnswerToCollectionIfMissing(
    formAnswerCollection: IFormAnswer[],
    ...formAnswersToCheck: (IFormAnswer | null | undefined)[]
  ): IFormAnswer[] {
    const formAnswers: IFormAnswer[] = formAnswersToCheck.filter(isPresent);
    if (formAnswers.length > 0) {
      const formAnswerCollectionIdentifiers = formAnswerCollection.map(formAnswerItem => getFormAnswerIdentifier(formAnswerItem)!);
      const formAnswersToAdd = formAnswers.filter(formAnswerItem => {
        const formAnswerIdentifier = getFormAnswerIdentifier(formAnswerItem);
        if (formAnswerIdentifier == null || formAnswerCollectionIdentifiers.includes(formAnswerIdentifier)) {
          return false;
        }
        formAnswerCollectionIdentifiers.push(formAnswerIdentifier);
        return true;
      });
      return [...formAnswersToAdd, ...formAnswerCollection];
    }
    return formAnswerCollection;
  }

  protected convertDateFromClient(formAnswer: IFormAnswer): IFormAnswer {
    return Object.assign({}, formAnswer, {
      formAnswerLocalDate: formAnswer.formAnswerLocalDate?.isValid() ? formAnswer.formAnswerLocalDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.formAnswerLocalDate = res.body.formAnswerLocalDate ? dayjs(res.body.formAnswerLocalDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((formAnswer: IFormAnswer) => {
        formAnswer.formAnswerLocalDate = formAnswer.formAnswerLocalDate ? dayjs(formAnswer.formAnswerLocalDate) : undefined;
      });
    }
    return res;
  }
}
