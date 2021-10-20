import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQuestionOption, QuestionOption } from '../question-option.model';

import { QuestionOptionService } from './question-option.service';

describe('Service Tests', () => {
  describe('QuestionOption Service', () => {
    let service: QuestionOptionService;
    let httpMock: HttpTestingController;
    let elemDefault: IQuestionOption;
    let expectedResult: IQuestionOption | IQuestionOption[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(QuestionOptionService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        questionOptionsCode: 'AAAAAAA',
        questionOptionsText: 'AAAAAAA',
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

      it('should create a QuestionOption', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new QuestionOption()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a QuestionOption', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            questionOptionsCode: 'BBBBBB',
            questionOptionsText: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a QuestionOption', () => {
        const patchObject = Object.assign(
          {
            questionOptionsCode: 'BBBBBB',
            questionOptionsText: 'BBBBBB',
          },
          new QuestionOption()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of QuestionOption', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            questionOptionsCode: 'BBBBBB',
            questionOptionsText: 'BBBBBB',
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

      it('should delete a QuestionOption', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addQuestionOptionToCollectionIfMissing', () => {
        it('should add a QuestionOption to an empty array', () => {
          const questionOption: IQuestionOption = { id: 123 };
          expectedResult = service.addQuestionOptionToCollectionIfMissing([], questionOption);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(questionOption);
        });

        it('should not add a QuestionOption to an array that contains it', () => {
          const questionOption: IQuestionOption = { id: 123 };
          const questionOptionCollection: IQuestionOption[] = [
            {
              ...questionOption,
            },
            { id: 456 },
          ];
          expectedResult = service.addQuestionOptionToCollectionIfMissing(questionOptionCollection, questionOption);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a QuestionOption to an array that doesn't contain it", () => {
          const questionOption: IQuestionOption = { id: 123 };
          const questionOptionCollection: IQuestionOption[] = [{ id: 456 }];
          expectedResult = service.addQuestionOptionToCollectionIfMissing(questionOptionCollection, questionOption);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(questionOption);
        });

        it('should add only unique QuestionOption to an array', () => {
          const questionOptionArray: IQuestionOption[] = [{ id: 123 }, { id: 456 }, { id: 79636 }];
          const questionOptionCollection: IQuestionOption[] = [{ id: 123 }];
          expectedResult = service.addQuestionOptionToCollectionIfMissing(questionOptionCollection, ...questionOptionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const questionOption: IQuestionOption = { id: 123 };
          const questionOption2: IQuestionOption = { id: 456 };
          expectedResult = service.addQuestionOptionToCollectionIfMissing([], questionOption, questionOption2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(questionOption);
          expect(expectedResult).toContain(questionOption2);
        });

        it('should accept null and undefined values', () => {
          const questionOption: IQuestionOption = { id: 123 };
          expectedResult = service.addQuestionOptionToCollectionIfMissing([], null, questionOption, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(questionOption);
        });

        it('should return initial array if no QuestionOption is added', () => {
          const questionOptionCollection: IQuestionOption[] = [{ id: 123 }];
          expectedResult = service.addQuestionOptionToCollectionIfMissing(questionOptionCollection, undefined, null);
          expect(expectedResult).toEqual(questionOptionCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
