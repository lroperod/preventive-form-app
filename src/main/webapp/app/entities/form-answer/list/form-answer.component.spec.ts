import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { FormAnswerService } from '../service/form-answer.service';

import { FormAnswerComponent } from './form-answer.component';

describe('Component Tests', () => {
  describe('FormAnswer Management Component', () => {
    let comp: FormAnswerComponent;
    let fixture: ComponentFixture<FormAnswerComponent>;
    let service: FormAnswerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FormAnswerComponent],
      })
        .overrideTemplate(FormAnswerComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FormAnswerComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(FormAnswerService);

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
      expect(comp.formAnswers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
