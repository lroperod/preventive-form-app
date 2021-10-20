import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { QuestionOptionService } from '../service/question-option.service';

import { QuestionOptionComponent } from './question-option.component';

describe('Component Tests', () => {
  describe('QuestionOption Management Component', () => {
    let comp: QuestionOptionComponent;
    let fixture: ComponentFixture<QuestionOptionComponent>;
    let service: QuestionOptionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [QuestionOptionComponent],
      })
        .overrideTemplate(QuestionOptionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuestionOptionComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(QuestionOptionService);

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
      expect(comp.questionOptions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
