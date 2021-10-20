import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFormAnswer, FormAnswer } from '../form-answer.model';

import { FormAnswerService } from './form-answer.service';

describe('Service Tests', () => {
  describe('FormAnswer Service', () => {
    let service: FormAnswerService;
    let httpMock: HttpTestingController;
    let elemDefault: IFormAnswer;
    let expectedResult: IFormAnswer | IFormAnswer[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FormAnswerService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        formAnswerLocalDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            formAnswerLocalDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a FormAnswer', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            formAnswerLocalDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            formAnswerLocalDate: currentDate,
          },
          returnedFromService
        );

        service.create(new FormAnswer()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FormAnswer', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            formAnswerLocalDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            formAnswerLocalDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a FormAnswer', () => {
        const patchObject = Object.assign(
          {
            formAnswerLocalDate: currentDate.format(DATE_FORMAT),
          },
          new FormAnswer()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            formAnswerLocalDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FormAnswer', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            formAnswerLocalDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            formAnswerLocalDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a FormAnswer', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFormAnswerToCollectionIfMissing', () => {
        it('should add a FormAnswer to an empty array', () => {
          const formAnswer: IFormAnswer = { id: 123 };
          expectedResult = service.addFormAnswerToCollectionIfMissing([], formAnswer);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(formAnswer);
        });

        it('should not add a FormAnswer to an array that contains it', () => {
          const formAnswer: IFormAnswer = { id: 123 };
          const formAnswerCollection: IFormAnswer[] = [
            {
              ...formAnswer,
            },
            { id: 456 },
          ];
          expectedResult = service.addFormAnswerToCollectionIfMissing(formAnswerCollection, formAnswer);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FormAnswer to an array that doesn't contain it", () => {
          const formAnswer: IFormAnswer = { id: 123 };
          const formAnswerCollection: IFormAnswer[] = [{ id: 456 }];
          expectedResult = service.addFormAnswerToCollectionIfMissing(formAnswerCollection, formAnswer);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(formAnswer);
        });

        it('should add only unique FormAnswer to an array', () => {
          const formAnswerArray: IFormAnswer[] = [{ id: 123 }, { id: 456 }, { id: 75893 }];
          const formAnswerCollection: IFormAnswer[] = [{ id: 123 }];
          expectedResult = service.addFormAnswerToCollectionIfMissing(formAnswerCollection, ...formAnswerArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const formAnswer: IFormAnswer = { id: 123 };
          const formAnswer2: IFormAnswer = { id: 456 };
          expectedResult = service.addFormAnswerToCollectionIfMissing([], formAnswer, formAnswer2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(formAnswer);
          expect(expectedResult).toContain(formAnswer2);
        });

        it('should accept null and undefined values', () => {
          const formAnswer: IFormAnswer = { id: 123 };
          expectedResult = service.addFormAnswerToCollectionIfMissing([], null, formAnswer, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(formAnswer);
        });

        it('should return initial array if no FormAnswer is added', () => {
          const formAnswerCollection: IFormAnswer[] = [{ id: 123 }];
          expectedResult = service.addFormAnswerToCollectionIfMissing(formAnswerCollection, undefined, null);
          expect(expectedResult).toEqual(formAnswerCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
