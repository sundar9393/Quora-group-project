package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    EntityManager manager;

    public QuestionEntity createQuestion(QuestionEntity question) {
        manager.persist(question);
        return question;
    }

    public List<QuestionEntity> getAllQuestions() {
        return manager.createNamedQuery("getAllQuestions", QuestionEntity.class).getResultList();
    }
}
