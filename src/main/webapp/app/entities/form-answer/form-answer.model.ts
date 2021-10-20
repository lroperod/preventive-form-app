import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IQuestionAnswer } from 'app/entities/question-answer/question-answer.model';
import { IForm } from 'app/entities/form/form.model';

export interface IFormAnswer {
  id?: number;
  formAnswerLocalDate?: dayjs.Dayjs | null;
  user?: IUser | null;
  questionAnswers?: IQuestionAnswer[] | null;
  form?: IForm | null;
}

export class FormAnswer implements IFormAnswer {
  constructor(
    public id?: number,
    public formAnswerLocalDate?: dayjs.Dayjs | null,
    public user?: IUser | null,
    public questionAnswers?: IQuestionAnswer[] | null,
    public form?: IForm | null
  ) {}
}

export function getFormAnswerIdentifier(formAnswer: IFormAnswer): number | undefined {
  return formAnswer.id;
}
