package com.lroperod.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QuestionAnswer.
 */
@Entity
@Table(name = "question_answer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QuestionAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "answer_code")
    private String answerCode;

    @Column(name = "answer_text")
    private String answerText;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "questionAnswers", "form" }, allowSetters = true)
    private FormAnswer formAnswer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QuestionAnswer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswerCode() {
        return this.answerCode;
    }

    public QuestionAnswer answerCode(String answerCode) {
        this.setAnswerCode(answerCode);
        return this;
    }

    public void setAnswerCode(String answerCode) {
        this.answerCode = answerCode;
    }

    public String getAnswerText() {
        return this.answerText;
    }

    public QuestionAnswer answerText(String answerText) {
        this.setAnswerText(answerText);
        return this;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public FormAnswer getFormAnswer() {
        return this.formAnswer;
    }

    public void setFormAnswer(FormAnswer formAnswer) {
        this.formAnswer = formAnswer;
    }

    public QuestionAnswer formAnswer(FormAnswer formAnswer) {
        this.setFormAnswer(formAnswer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionAnswer)) {
            return false;
        }
        return id != null && id.equals(((QuestionAnswer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionAnswer{" +
            "id=" + getId() +
            ", answerCode='" + getAnswerCode() + "'" +
            ", answerText='" + getAnswerText() + "'" +
            "}";
    }
}
