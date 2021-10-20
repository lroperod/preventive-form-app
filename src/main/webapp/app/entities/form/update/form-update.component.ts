import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IForm, Form } from '../form.model';
import { FormService } from '../service/form.service';

@Component({
  selector: 'pfa-form-update',
  templateUrl: './form-update.component.html',
})
export class FormUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    formName: [null, [Validators.required]],
    description: [],
  });

  constructor(protected formService: FormService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ form }) => {
      this.updateForm(form);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const form = this.createFromForm();
    if (form.id !== undefined) {
      this.subscribeToSaveResponse(this.formService.update(form));
    } else {
      this.subscribeToSaveResponse(this.formService.create(form));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IForm>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(form: IForm): void {
    this.editForm.patchValue({
      id: form.id,
      formName: form.formName,
      description: form.description,
    });
  }

  protected createFromForm(): IForm {
    return {
      ...new Form(),
      id: this.editForm.get(['id'])!.value,
      formName: this.editForm.get(['formName'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
