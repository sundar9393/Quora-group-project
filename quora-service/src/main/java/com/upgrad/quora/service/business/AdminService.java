package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationService authenticationService;


    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity deleteUserAsAdmin(final String userUuid, final String accessToken) throws UserNotFoundException, AuthorizationFailedException {
        UserAuthTokenEntity userAuthToken = authenticationService.signInValidation(accessToken);
        if(!userAuthToken.getUser().getRole().equals("admin")) {
            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
        }

        UserEntity userEntity = userDao.getUserByUuid(userUuid);

        if (userEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid to be deleted does not exist");
        }

        //Delete User Profile for the User
        return userDao.deleteUser(userEntity);


    }

}