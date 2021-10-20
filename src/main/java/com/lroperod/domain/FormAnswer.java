package com.lroperod.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FormAnswer.
 */
@Entity
@Table(name = "form_answer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FormAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "form_answer_local_date")
    private LocalDate formAnswerLocalDate;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "formAnswer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "answer", "formAnswer" }, allowSetters = true)
    private Set<QuestionAnswer> questionAnswers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "questions", "formAnswers" }, allowSetters = true)
    private Form form;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FormAnswer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFormAnswerLocalDate() {
        return this.formAnswerLocalDate;
    }

    public FormAnswer formAnswerLocalDate(LocalDate formAnswerLocalDate) {
        this.setFormAnswerLocalDate(formAnswerLocalDate);
        return this;
    }

    public void setFormAnswerLocalDate(LocalDate formAnswerLocalDate) {
        this.formAnswerLocalDate = formAnswerLocalDate;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FormAnswer user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<QuestionAnswer> getQuestionAnswers() {
        return this.questionAnswers;
    }

    public void setQuestionAnswers(Set<QuestionAnswer> questionAnswers) {
        if (this.questionAnswers != null) {
            this.questionAnswers.forEach(i -> i.setFormAnswer(null));
        }
        if (questionAnswers != null) {
            questionAnswers.forEach(i -> i.setFormAnswer(this));
        }
        this.questionAnswers = questionAnswers;
    }

    public FormAnswer questionAnswers(Set<QuestionAnswer> questionAnswers) {
        this.setQuestionAnswers(questionAnswers);
        return this;
    }

    public FormAnswer addQuestionAnswer(QuestionAnswer questionAnswer) {
        this.questionAnswers.add(questionAnswer);
        questionAnswer.setFormAnswer(this);
        return this;
    }

    public FormAnswer removeQuestionAnswer(QuestionAnswer questionAnswer) {
        this.questionAnswers.remove(questionAnswer);
        questionAnswer.setFormAnswer(null);
        return this;
    }

    public Form getForm() {
        return this.form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public FormAnswer form(Form form) {
        this.setForm(form);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormAnswer)) {
            return false;
        }
        return id != null && id.equals(((FormAnswer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormAnswer{" +
            "id=" + getId() +
            ", formAnswerLocalDate='" + getFormAnswerLocalDate() + "'" +
            "}";
    }
}
