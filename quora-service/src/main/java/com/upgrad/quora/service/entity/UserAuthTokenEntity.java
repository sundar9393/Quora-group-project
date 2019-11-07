package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Entity
@Table(name = "user_auth")
@NamedQueries(
        {
                @NamedQuery(name = "authByAccessToken", query = "select u from UserAuthTokenEntity u where u.accessToken = :accesstoken"),
                @NamedQuery(name = "authTokenByUserId", query = "select u from UserAuthTokenEntity u where u.user_id = :user_id")
        }
)
public class UserAuthTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    private String uuid;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "access_token")
    @Size(max = 500)
    @NotNull
    private String accessToken;

    @Column(name = "expires_at")
    @NotNull
    private ZonedDateTime expiryTime;

    @Column(name = "login_at")
    @NotNull
    private ZonedDateTime loginTime;

    @Column(name = "logout_at")
    private ZonedDateTime logutTime;

    public UserAuthTokenEntity() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ZonedDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(ZonedDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public ZonedDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(ZonedDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public ZonedDateTime getLogutTime() {
        return logutTime;
    }

    public void setLogutTime(ZonedDateTime logutTime) {
        this.logutTime = logutTime;
    }
}
