package com.lroperod.service.dto;

import com.lroperod.domain.enumeration.QuestionType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.lroperod.domain.Questions} entity.
 */
public class QuestionsDTO implements Serializable {

    private Long id;

    private String questionCode;

    private String questionText;

    private QuestionType questionType;

    private FormDTO form;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public FormDTO getForm() {
        return form;
    }

    public void setForm(FormDTO form) {
        this.form = form;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionsDTO)) {
            return false;
        }

        QuestionsDTO questionsDTO = (QuestionsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionsDTO{" +
            "id=" + getId() +
            ", questionCode='" + getQuestionCode() + "'" +
            ", questionText='" + getQuestionText() + "'" +
            ", questionType='" + getQuestionType() + "'" +
            ", form=" + getForm() +
            "}";
    }
}
