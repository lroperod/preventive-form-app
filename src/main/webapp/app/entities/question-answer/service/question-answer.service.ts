import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuestionAnswer, getQuestionAnswerIdentifier } from '../question-answer.model';

export type EntityResponseType = HttpResponse<IQuestionAnswer>;
export type EntityArrayResponseType = HttpResponse<IQuestionAnswer[]>;

@Injectable({ providedIn: 'root' })
export class QuestionAnswerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/question-answers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(questionAnswer: IQuestionAnswer): Observable<EntityResponseType> {
    return this.http.post<IQuestionAnswer>(this.resourceUrl, questionAnswer, { observe: 'response' });
  }

  update(questionAnswer: IQuestionAnswer): Observable<EntityResponseType> {
    return this.http.put<IQuestionAnswer>(`${this.resourceUrl}/${getQuestionAnswerIdentifier(questionAnswer) as number}`, questionAnswer, {
      observe: 'response',
    });
  }

  partialUpdate(questionAnswer: IQuestionAnswer): Observable<EntityResponseType> {
    return this.http.patch<IQuestionAnswer>(
      `${this.resourceUrl}/${getQuestionAnswerIdentifier(questionAnswer) as number}`,
      questionAnswer,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuestionAnswer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuestionAnswer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addQuestionAnswerToCollectionIfMissing(
    questionAnswerCollection: IQuestionAnswer[],
    ...questionAnswersToCheck: (IQuestionAnswer | null | undefined)[]
  ): IQuestionAnswer[] {
    const questionAnswers: IQuestionAnswer[] = questionAnswersToCheck.filter(isPresent);
    if (questionAnswers.length > 0) {
      const questionAnswerCollectionIdentifiers = questionAnswerCollection.map(
        questionAnswerItem => getQuestionAnswerIdentifier(questionAnswerItem)!
      );
      const questionAnswersToAdd = questionAnswers.filter(questionAnswerItem => {
        const questionAnswerIdentifier = getQuestionAnswerIdentifier(questionAnswerItem);
        if (questionAnswerIdentifier == null || questionAnswerCollectionIdentifiers.includes(questionAnswerIdentifier)) {
          return false;
        }
        questionAnswerCollectionIdentifiers.push(questionAnswerIdentifier);
        return true;
      });
      return [...questionAnswersToAdd, ...questionAnswerCollection];
    }
    return questionAnswerCollection;
  }
}
