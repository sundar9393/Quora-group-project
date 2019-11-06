package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
        return manager.createNamedQuery("allQuestions", QuestionEntity.class).getResultList();
    }

    public QuestionEntity getQuestionByUuid(final String uuid) {
        try {
            return manager.createNamedQuery("questionByUuid", QuestionEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public QuestionEntity editQuestionContent(QuestionEntity questionEntity) {
        return manager.merge(questionEntity);
    }

    public QuestionEntity deleteQuestion(QuestionEntity questionEntity) {
        manager.remove(questionEntity);
        return questionEntity;
    }

    public List<QuestionEntity> getAllQuestionsByUser(final Integer userId) {
        return manager.createNamedQuery("questionByUserid",QuestionEntity.class).setParameter("userId",userId).getResultList();
    }
}
