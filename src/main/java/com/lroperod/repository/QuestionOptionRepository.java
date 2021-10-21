package com.lroperod.repository;

import com.lroperod.domain.QuestionOption;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the QuestionOption entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {}
