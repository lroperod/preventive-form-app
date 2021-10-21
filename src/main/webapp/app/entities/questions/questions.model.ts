import { IQuestionOption } from 'app/entities/question-option/question-option.model';
import { IQuestionAnswer } from 'app/entities/question-answer/question-answer.model';
import { IForm } from 'app/entities/form/form.model';
import { QuestionType } from 'app/entities/enumerations/question-type.model';

export interface IQuestions {
  id?: number;
  questionCode?: string | null;
  questionText?: string | null;
  questionType?: QuestionType | null;
  questions?: IQuestionOption[] | null;
  questionAnswer?: IQuestionAnswer | null;
  form?: IForm | null;
}

export class Questions implements IQuestions {
  constructor(
    public id?: number,
    public questionCode?: string | null,
    public questionText?: string | null,
    public questionType?: QuestionType | null,
    public questions?: IQuestionOption[] | null,
    public questionAnswer?: IQuestionAnswer | null,
    public form?: IForm | null
  ) {}
}

export function getQuestionsIdentifier(questions: IQuestions): number | undefined {
  return questions.id;
}
