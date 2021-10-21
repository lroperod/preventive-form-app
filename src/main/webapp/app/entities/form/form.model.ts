import { IQuestions } from 'app/entities/questions/questions.model';
import { IFormAnswer } from 'app/entities/form-answer/form-answer.model';

export interface IForm {
  id?: number;
  formName?: string;
  description?: string | null;
  questions?: IQuestions[] | null;
  formAnswers?: IFormAnswer[] | null;
}

export class Form implements IForm {
  constructor(
    public id?: number,
    public formName?: string,
    public description?: string | null,
    public questions?: IQuestions[] | null,
    public formAnswers?: IFormAnswer[] | null
  ) {}
}

export function getFormIdentifier(form: IForm): number | undefined {
  return form.id;
}
