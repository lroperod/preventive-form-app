import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQuestionAnswer, QuestionAnswer } from '../question-answer.model';

import { QuestionAnswerService } from './question-answer.service';

describe('Service Tests', () => {
  describe('QuestionAnswer Service', () => {
    let service: QuestionAnswerService;
    let httpMock: HttpTestingController;
    let elemDefault: IQuestionAnswer;
    let expectedResult: IQuestionAnswer | IQuestionAnswer[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(QuestionAnswerService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        answerCode: 'AAAAAAA',
        answerText: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a QuestionAnswer', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new QuestionAnswer()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a QuestionAnswer', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            answerCode: 'BBBBBB',
            answerText: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a QuestionAnswer', () => {
        const patchObject = Object.assign({}, new QuestionAnswer());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of QuestionAnswer', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            answerCode: 'BBBBBB',
            answerText: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a QuestionAnswer', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addQuestionAnswerToCollectionIfMissing', () => {
        it('should add a QuestionAnswer to an empty array', () => {
          const questionAnswer: IQuestionAnswer = { id: 123 };
          expectedResult = service.addQuestionAnswerToCollectionIfMissing([], questionAnswer);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(questionAnswer);
        });

        it('should not add a QuestionAnswer to an array that contains it', () => {
          const questionAnswer: IQuestionAnswer = { id: 123 };
          const questionAnswerCollection: IQuestionAnswer[] = [
            {
              ...questionAnswer,
            },
            { id: 456 },
          ];
          expectedResult = service.addQuestionAnswerToCollectionIfMissing(questionAnswerCollection, questionAnswer);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a QuestionAnswer to an array that doesn't contain it", () => {
          const questionAnswer: IQuestionAnswer = { id: 123 };
          const questionAnswerCollection: IQuestionAnswer[] = [{ id: 456 }];
          expectedResult = service.addQuestionAnswerToCollectionIfMissing(questionAnswerCollection, questionAnswer);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(questionAnswer);
        });

        it('should add only unique QuestionAnswer to an array', () => {
          const questionAnswerArray: IQuestionAnswer[] = [{ id: 123 }, { id: 456 }, { id: 87769 }];
          const questionAnswerCollection: IQuestionAnswer[] = [{ id: 123 }];
          expectedResult = service.addQuestionAnswerToCollectionIfMissing(questionAnswerCollection, ...questionAnswerArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const questionAnswer: IQuestionAnswer = { id: 123 };
          const questionAnswer2: IQuestionAnswer = { id: 456 };
          expectedResult = service.addQuestionAnswerToCollectionIfMissing([], questionAnswer, questionAnswer2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(questionAnswer);
          expect(expectedResult).toContain(questionAnswer2);
        });

        it('should accept null and undefined values', () => {
          const questionAnswer: IQuestionAnswer = { id: 123 };
          expectedResult = service.addQuestionAnswerToCollectionIfMissing([], null, questionAnswer, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(questionAnswer);
        });

        it('should return initial array if no QuestionAnswer is added', () => {
          const questionAnswerCollection: IQuestionAnswer[] = [{ id: 123 }];
          expectedResult = service.addQuestionAnswerToCollectionIfMissing(questionAnswerCollection, undefined, null);
          expect(expectedResult).toEqual(questionAnswerCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
