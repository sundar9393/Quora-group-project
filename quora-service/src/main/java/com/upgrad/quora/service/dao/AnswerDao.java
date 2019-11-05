package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager manager;

    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        manager.persist(answerEntity);
        return answerEntity;
    }
}
