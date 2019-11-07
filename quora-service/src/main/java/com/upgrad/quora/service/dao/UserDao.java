package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity getUserByUsername(final String userName) {
        try {
            return entityManager.createNamedQuery("userByUsername", UserEntity.class).setParameter("username", userName).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }

    }


    public UserEntity getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email",email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }

    }

    public UserEntity getUserByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }
    }


    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }


    public UserAuthTokenEntity createAuthToken(UserAuthTokenEntity authTokenEntity) {
        entityManager.persist(authTokenEntity);
        return authTokenEntity;
    }


    public UserAuthTokenEntity getAuthToken(String accessToken) {
        try {
          return entityManager.createNamedQuery("authByAccessToken", UserAuthTokenEntity.class).setParameter("accesstoken", accessToken).getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }
    }


    public UserEntity deleteUser(final UserEntity userEntity) {
        entityManager.remove(userEntity);
        return userEntity;
    }


}
