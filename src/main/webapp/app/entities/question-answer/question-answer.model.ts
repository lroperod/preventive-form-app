import { IQuestions } from 'app/entities/questions/questions.model';
import { IFormAnswer } from 'app/entities/form-answer/form-answer.model';

export interface IQuestionAnswer {
  id?: number;
  answerCode?: string | null;
  answerText?: string | null;
  answer?: IQuestions | null;
  formAnswer?: IFormAnswer | null;
}

export class QuestionAnswer implements IQuestionAnswer {
  constructor(
    public id?: number,
    public answerCode?: string | null,
    public answerText?: string | null,
    public answer?: IQuestions | null,
    public formAnswer?: IFormAnswer | null
  ) {}
}

export function getQuestionAnswerIdentifier(questionAnswer: IQuestionAnswer): number | undefined {
  return questionAnswer.id;
}
