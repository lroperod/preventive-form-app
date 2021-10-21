package com.lroperod.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lroperod.domain.enumeration.QuestionType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Questions.
 */
@Entity
@Table(name = "questions")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Questions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "question_code")
    private String questionCode;

    @Column(name = "question_text")
    private String questionText;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type")
    private QuestionType questionType;

    @OneToMany(mappedBy = "questions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "questions" }, allowSetters = true)
    private Set<QuestionOption> questions = new HashSet<>();

    @JsonIgnoreProperties(value = { "questions", "formAnswer" }, allowSetters = true)
    @OneToOne(mappedBy = "questions")
    private QuestionAnswer questionAnswer;

    @ManyToOne
    @JsonIgnoreProperties(value = { "questions", "formAnswers" }, allowSetters = true)
    private Form form;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Questions id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionCode() {
        return this.questionCode;
    }

    public Questions questionCode(String questionCode) {
        this.setQuestionCode(questionCode);
        return this;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public String getQuestionText() {
        return this.questionText;
    }

    public Questions questionText(String questionText) {
        this.setQuestionText(questionText);
        return this;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public QuestionType getQuestionType() {
        return this.questionType;
    }

    public Questions questionType(QuestionType questionType) {
        this.setQuestionType(questionType);
        return this;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public Set<QuestionOption> getQuestions() {
        return this.questions;
    }

    public void setQuestions(Set<QuestionOption> questionOptions) {
        if (this.questions != null) {
            this.questions.forEach(i -> i.setQuestions(null));
        }
        if (questionOptions != null) {
            questionOptions.forEach(i -> i.setQuestions(this));
        }
        this.questions = questionOptions;
    }

    public Questions questions(Set<QuestionOption> questionOptions) {
        this.setQuestions(questionOptions);
        return this;
    }

    public Questions addQuestions(QuestionOption questionOption) {
        this.questions.add(questionOption);
        questionOption.setQuestions(this);
        return this;
    }

    public Questions removeQuestions(QuestionOption questionOption) {
        this.questions.remove(questionOption);
        questionOption.setQuestions(null);
        return this;
    }

    public QuestionAnswer getQuestionAnswer() {
        return this.questionAnswer;
    }

    public void setQuestionAnswer(QuestionAnswer questionAnswer) {
        if (this.questionAnswer != null) {
            this.questionAnswer.setQuestions(null);
        }
        if (questionAnswer != null) {
            questionAnswer.setQuestions(this);
        }
        this.questionAnswer = questionAnswer;
    }

    public Questions questionAnswer(QuestionAnswer questionAnswer) {
        this.setQuestionAnswer(questionAnswer);
        return this;
    }

    public Form getForm() {
        return this.form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Questions form(Form form) {
        this.setForm(form);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Questions)) {
            return false;
        }
        return id != null && id.equals(((Questions) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Questions{" +
            "id=" + getId() +
            ", questionCode='" + getQuestionCode() + "'" +
            ", questionText='" + getQuestionText() + "'" +
            ", questionType='" + getQuestionType() + "'" +
            "}";
    }
}
