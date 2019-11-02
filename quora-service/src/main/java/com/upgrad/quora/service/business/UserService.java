package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    PasswordCryptographyProvider cryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {
        //I need to check whether the username is already there
        UserEntity existingUsername = userDao.getUserByUsername(userEntity.getUserName());
        if(existingUsername!=null){
            throw new SignUpRestrictedException("SGR-001","Try any other Username, this Username has already been taken");
        }
        UserEntity existingEmail = userDao.getUserByEmail(userEntity.getEmail());
        if(existingEmail!=null) {
            throw new SignUpRestrictedException("SGR-002","This user has already been registered, try with any other emailId");
        }
        //if code reaches this part, means the user name is not taken, the user entity needs to be persisted in the DB
        //before persisting to the DB need to encrypt the password, set salt and set the uuid
        String[] encryptedValues = cryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedValues[0]);
        userEntity.setPassword(encryptedValues[1]);
        userEntity.setUuid(UUID.randomUUID().toString());
        return userDao.createUser(userEntity);
    }

    public UserEntity signout(final String authorization) throws SignOutRestrictedException {
        //I need to search user auth token table
        UserAuthTokenEntity userDaoAuthToken = userDao.getAuthToken(authorization);
        if(userDaoAuthToken == null) {
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }
        //update the logout time and return the user entity
        userDaoAuthToken.setLogutTime(ZonedDateTime.now());
        return userDaoAuthToken.getUser();
    }

    public UserEntity getUser(final String uuid, final String accessToken) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity userAuthToken = userDao.getAuthToken(accessToken);
        if(userAuthToken == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if(userAuthToken.getLoginTime().isBefore(userAuthToken.getLogutTime())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
        }
        UserEntity userEntity = userDao.getUserByUuid(uuid);
        if(userEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        }
        return userEntity;

    }
}
