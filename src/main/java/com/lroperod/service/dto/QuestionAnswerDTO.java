package com.lroperod.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.lroperod.domain.QuestionAnswer} entity.
 */
public class QuestionAnswerDTO implements Serializable {

    private Long id;

    private String answerCode;

    private String answerText;

    private QuestionsDTO questions;

    private FormAnswerDTO formAnswer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswerCode() {
        return answerCode;
    }

    public void setAnswerCode(String answerCode) {
        this.answerCode = answerCode;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public QuestionsDTO getQuestions() {
        return questions;
    }

    public void setQuestions(QuestionsDTO questions) {
        this.questions = questions;
    }

    public FormAnswerDTO getFormAnswer() {
        return formAnswer;
    }

    public void setFormAnswer(FormAnswerDTO formAnswer) {
        this.formAnswer = formAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionAnswerDTO)) {
            return false;
        }

        QuestionAnswerDTO questionAnswerDTO = (QuestionAnswerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionAnswerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionAnswerDTO{" +
            "id=" + getId() +
            ", answerCode='" + getAnswerCode() + "'" +
            ", answerText='" + getAnswerText() + "'" +
            ", questions=" + getQuestions() +
            ", formAnswer=" + getFormAnswer() +
            "}";
    }
}
