import { IForm } from 'app/entities/form/form.model';
import { QuestionType } from 'app/entities/enumerations/question-type.model';

export interface IQuestions {
  id?: number;
  questionCode?: string | null;
  questionText?: string | null;
  questionType?: QuestionType | null;
  form?: IForm | null;
}

export class Questions implements IQuestions {
  constructor(
    public id?: number,
    public questionCode?: string | null,
    public questionText?: string | null,
    public questionType?: QuestionType | null,
    public form?: IForm | null
  ) {}
}

export function getQuestionsIdentifier(questions: IQuestions): number | undefined {
  return questions.id;
}
