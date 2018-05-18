package ru.discloud.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.discloud.auth.domain.User;
import ru.discloud.auth.domain.UserToken;
import ru.discloud.auth.exception.TokenExpiredException;
import ru.discloud.auth.exception.TokenInvalidException;
import ru.discloud.auth.exception.UserCredentialsException;
import ru.discloud.auth.repository.redis.UserTokenDevice;
import ru.discloud.auth.repository.redis.UserTokenRepository;
import ru.discloud.auth.web.model.UserTokenInitialRequest;
import ru.discloud.auth.web.model.UserTokenRefreshRequest;
import ru.discloud.auth.web.model.UserTokenRemoveRequest;
import ru.discloud.auth.web.model.UserTokenRequest;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;

@Service
public class UserTokenServiceImpl implements UserTokenService {
    private final static String ACCESS_TOKEN_KEY = "IHu8HmG8ra";
    private final static String REFRESH_TOKEN_KEY = "FWskhCes6p";

    private final UserService userService;
    private final UserTokenRepository userTokenRepository;

    @Autowired
    public UserTokenServiceImpl(UserService userService, UserTokenRepository userTokenRepository) {
        this.userService = userService;
        this.userTokenRepository = userTokenRepository;
    }

    @Override
    public UserToken create(UserTokenInitialRequest userTokenInitialRequest) throws UserCredentialsException {
        User user = userService.checkCredentials(userTokenInitialRequest.getUsername(), userTokenInitialRequest.getPassword());
        if (user != null) {
            UserToken userToken = generateToken(user, userTokenInitialRequest.getDeviceId());
            return userTokenRepository.save(userToken);
        } else {
            throw new UserCredentialsException("user credentials not valid");
        }
    }

    @Override
    public UserToken refresh(UserTokenRefreshRequest userTokenRefreshRequest) throws TokenExpiredException, TokenInvalidException {
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(DatatypeConverter.printBase64Binary(REFRESH_TOKEN_KEY.getBytes()))
                .parseClaimsJws(userTokenRefreshRequest.getRefreshToken());
        UserTokenDevice userDevice = userTokenRepository.getRefreshToken(userTokenRefreshRequest.getRefreshToken())
                .orElseThrow(() -> new TokenInvalidException("Refresh token '{" + userTokenRefreshRequest.getRefreshToken() + "}' not found"));
        userTokenRepository.delete(userDevice);
        if ((new Date()).after(jws.getBody().getExpiration())) {
            throw new TokenExpiredException("Refresh token expired");
        }
        User user = userService.findByUsername(jws.getBody().getSubject());
        UserToken userToken = generateToken(user, userDevice.getDeviceId());
        return userTokenRepository.save(userToken);
    }

    @Override
    public User checkAccessToken(UserTokenRequest userTokenRequest) throws TokenExpiredException, TokenInvalidException {
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(DatatypeConverter.printBase64Binary(ACCESS_TOKEN_KEY.getBytes()))
                .parseClaimsJws(userTokenRequest.getAccessToken());
        if ((new Date()).after(jws.getBody().getExpiration())) {
            throw new TokenExpiredException("ExpirationDate");
        }
        UserTokenDevice userTokenDevice = userTokenRepository.getAccessToken(userTokenRequest.getAccessToken())
                .orElseThrow(() -> new TokenInvalidException("Access token '{" + userTokenRequest.getAccessToken() + "}' not found"));
        return userService.findByUsername(jws.getBody().getSubject());
    }

    @Override
    public void deleteToken(UserTokenRemoveRequest userTokenRequest) {
        UserTokenDevice userTokenDevice = new UserTokenDevice(userTokenRequest.getUserId(), userTokenRequest.getDeviceId());
        String userToken = userTokenRepository.getUserTokenDevice(userTokenDevice);
        if (userToken == null) {
            throw new EntityNotFoundException("Token for userId '{" + userTokenDevice.getUserId() + "}' and deviceId '{" + userTokenDevice.getDeviceId() + "}' not found");
        }
        userTokenRepository.delete(userTokenDevice);
    }

    private UserToken generateToken(User user, String deviceId) {
        Date accessExpires = new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000L));
        Date refreshExpires = new Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000L));

        String accessToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(accessExpires)
                .signWith(SignatureAlgorithm.HS512, DatatypeConverter.printBase64Binary(ACCESS_TOKEN_KEY.getBytes()))
                .compact();
        String refreshToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(refreshExpires)
                .signWith(SignatureAlgorithm.HS512, DatatypeConverter.printBase64Binary(REFRESH_TOKEN_KEY.getBytes()))
                .compact();

        return new UserToken()
                .setUserId(user.getId())
                .setDeviceId(deviceId)
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .setAccessExpires(accessExpires)
                .setRefreshExpires(refreshExpires);
    }
}
