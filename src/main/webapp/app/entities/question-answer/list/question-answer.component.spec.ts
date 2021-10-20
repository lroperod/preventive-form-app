import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { QuestionAnswerService } from '../service/question-answer.service';

import { QuestionAnswerComponent } from './question-answer.component';

describe('Component Tests', () => {
  describe('QuestionAnswer Management Component', () => {
    let comp: QuestionAnswerComponent;
    let fixture: ComponentFixture<QuestionAnswerComponent>;
    let service: QuestionAnswerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [QuestionAnswerComponent],
      })
        .overrideTemplate(QuestionAnswerComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuestionAnswerComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(QuestionAnswerService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.questionAnswers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
