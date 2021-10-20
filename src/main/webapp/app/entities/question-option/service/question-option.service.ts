import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuestionOption, getQuestionOptionIdentifier } from '../question-option.model';

export type EntityResponseType = HttpResponse<IQuestionOption>;
export type EntityArrayResponseType = HttpResponse<IQuestionOption[]>;

@Injectable({ providedIn: 'root' })
export class QuestionOptionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/question-options');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(questionOption: IQuestionOption): Observable<EntityResponseType> {
    return this.http.post<IQuestionOption>(this.resourceUrl, questionOption, { observe: 'response' });
  }

  update(questionOption: IQuestionOption): Observable<EntityResponseType> {
    return this.http.put<IQuestionOption>(`${this.resourceUrl}/${getQuestionOptionIdentifier(questionOption) as number}`, questionOption, {
      observe: 'response',
    });
  }

  partialUpdate(questionOption: IQuestionOption): Observable<EntityResponseType> {
    return this.http.patch<IQuestionOption>(
      `${this.resourceUrl}/${getQuestionOptionIdentifier(questionOption) as number}`,
      questionOption,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuestionOption>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuestionOption[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addQuestionOptionToCollectionIfMissing(
    questionOptionCollection: IQuestionOption[],
    ...questionOptionsToCheck: (IQuestionOption | null | undefined)[]
  ): IQuestionOption[] {
    const questionOptions: IQuestionOption[] = questionOptionsToCheck.filter(isPresent);
    if (questionOptions.length > 0) {
      const questionOptionCollectionIdentifiers = questionOptionCollection.map(
        questionOptionItem => getQuestionOptionIdentifier(questionOptionItem)!
      );
      const questionOptionsToAdd = questionOptions.filter(questionOptionItem => {
        const questionOptionIdentifier = getQuestionOptionIdentifier(questionOptionItem);
        if (questionOptionIdentifier == null || questionOptionCollectionIdentifiers.includes(questionOptionIdentifier)) {
          return false;
        }
        questionOptionCollectionIdentifiers.push(questionOptionIdentifier);
        return true;
      });
      return [...questionOptionsToAdd, ...questionOptionCollection];
    }
    return questionOptionCollection;
  }
}
