import { IQuestions } from 'app/entities/questions/questions.model';

export interface IQuestionOption {
  id?: number;
  questionOptionsCode?: string | null;
  questionOptionsText?: string | null;
  questions?: IQuestions[] | null;
}

export class QuestionOption implements IQuestionOption {
  constructor(
    public id?: number,
    public questionOptionsCode?: string | null,
    public questionOptionsText?: string | null,
    public questions?: IQuestions[] | null
  ) {}
}

export function getQuestionOptionIdentifier(questionOption: IQuestionOption): number | undefined {
  return questionOption.id;
}
