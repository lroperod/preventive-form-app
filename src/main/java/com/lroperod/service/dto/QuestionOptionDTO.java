package com.lroperod.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.lroperod.domain.QuestionOption} entity.
 */
public class QuestionOptionDTO implements Serializable {

    private Long id;

    private String questionOptionsCode;

    private String questionOptionsText;

    private QuestionsDTO questions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionOptionsCode() {
        return questionOptionsCode;
    }

    public void setQuestionOptionsCode(String questionOptionsCode) {
        this.questionOptionsCode = questionOptionsCode;
    }

    public String getQuestionOptionsText() {
        return questionOptionsText;
    }

    public void setQuestionOptionsText(String questionOptionsText) {
        this.questionOptionsText = questionOptionsText;
    }

    public QuestionsDTO getQuestions() {
        return questions;
    }

    public void setQuestions(QuestionsDTO questions) {
        this.questions = questions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionOptionDTO)) {
            return false;
        }

        QuestionOptionDTO questionOptionDTO = (QuestionOptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionOptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionOptionDTO{" +
            "id=" + getId() +
            ", questionOptionsCode='" + getQuestionOptionsCode() + "'" +
            ", questionOptionsText='" + getQuestionOptionsText() + "'" +
            ", questions=" + getQuestions() +
            "}";
    }
}
