package ru.discloud.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.discloud.auth.domain.App;
import ru.discloud.auth.domain.AppToken;
import ru.discloud.auth.domain.User;
import ru.discloud.auth.exception.AppCredentialsException;
import ru.discloud.auth.exception.TokenExpiredException;
import ru.discloud.auth.exception.TokenInvalidException;
import ru.discloud.auth.exception.UserCredentialsException;
import ru.discloud.auth.repository.redis.AppTokenDevice;
import ru.discloud.auth.repository.redis.AppTokenRepository;
import ru.discloud.auth.web.model.AppTokenCodeRequest;
import ru.discloud.auth.web.model.AppTokenInitialRequest;
import ru.discloud.auth.web.model.AppTokenRefreshRequest;
import ru.discloud.auth.web.model.AppTokenRequest;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;

@Service
public class AppTokenServiceImpl implements AppTokenService {
    private final String ACCESS_TOKEN_KEY = "ihT51Su1QH";
    private final String REFRESH_TOKEN_KEY = "RsSYbTnk7M";

    private final AppService appService;
    private final UserService userService;
    private final AppTokenRepository appTokenRepository;

    @Autowired
    public AppTokenServiceImpl(AppService appService, UserService userService, AppTokenRepository appTokenRepository) {
        this.appService = appService;
        this.userService = userService;
        this.appTokenRepository = appTokenRepository;
    }

    @Override
    public AppToken create(AppTokenInitialRequest appTokenInitialRequest) throws UserCredentialsException {
        App app = appService.findById(appTokenInitialRequest.getAppId());
        if (app == null) {
            throw new EntityNotFoundException("App '{" + appTokenInitialRequest.getAppId() + "}' not found");
        }
        User user = userService.checkCredentials(appTokenInitialRequest.getUsername(), appTokenInitialRequest.getPassword());
        AppToken token = generateToken(app, user);
        return appTokenRepository.save(token);
    }

    @Override
    public AppToken findByAppCode(AppTokenCodeRequest appTokenCodeRequest) throws AppCredentialsException {
        App app = appService.findById(appTokenCodeRequest.getAppId());
        if (!app.getSecret().equals(appTokenCodeRequest.getAppSecret())) {
            throw new AppCredentialsException("App '{" + app.getId() + "}' secret not valid");
        }
        AppToken token = appTokenRepository.getTokenByAppCode(app.getId(), appTokenCodeRequest.getCode())
                .orElseThrow(() -> new EntityNotFoundException("Token with code '{" + appTokenCodeRequest.getCode() + "}' not found"));
        appTokenRepository.deleteAppCode(app.getId(), appTokenCodeRequest.getCode());
        return token;
    }

    @Override
    public AppToken refresh(AppTokenRefreshRequest appTokenRefreshRequest) throws Exception {
        App app = appService.findById(Integer.parseInt(appTokenRefreshRequest.getRefreshToken().substring(
                appTokenRefreshRequest.getRefreshToken().lastIndexOf(":") + 1)));
        Jws<Claims> jws = decryptAuthToken(REFRESH_TOKEN_KEY, appTokenRefreshRequest.getRefreshToken());
        if ((new Date()).after(jws.getBody().getExpiration())) {
            throw new TokenExpiredException("Token '{" + appTokenRefreshRequest.getRefreshToken() + "}' is expired");
        }
        User user = userService.findByUsername(jws.getBody().getSubject());
        return generateToken(app, user);
    }

    @Override
    public User checkAccessToken(AppTokenRequest appTokenRequest) throws TokenExpiredException, TokenInvalidException {
        AppTokenDevice tokenDevice = appTokenRepository.getAccessToken(appTokenRequest.getAccessToken())
                .orElse(null);
        if (tokenDevice == null) {
            throw new TokenInvalidException("Token '{" + appTokenRequest.getAccessToken() + "}' in stop list");
        }
        Jws<Claims> jws = decryptAuthToken(ACCESS_TOKEN_KEY, appTokenRequest.getAccessToken());
        if ((new Date()).after(jws.getBody().getExpiration())) {
            throw new TokenExpiredException("Token '{" + appTokenRequest.getAccessToken() + "}' is expired");
        }
        return userService.findByUsername(jws.getBody().getSubject());
    }

    @Override
    public void deleteToken(AppTokenRequest appTokenRequest) throws TokenInvalidException {
        AppTokenDevice tokenDevice = appTokenRepository.getAccessToken(appTokenRequest.getAccessToken())
                .orElse(null);
        if (tokenDevice == null) {
            throw new TokenInvalidException("Token '{" + appTokenRequest.getAccessToken() + "}' in stop list");
        }
        appTokenRepository.delete(tokenDevice);
    }

    private AppToken generateToken(App app, User user) {
        String accessTokenKey = app.getSecret() + ACCESS_TOKEN_KEY;
        String refreshTokenKey = app.getSecret() + REFRESH_TOKEN_KEY;

        Date accessExpires = new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000));
        Date refreshExpires = new Date(System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000L));

        String accessToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(accessExpires)
                .signWith(SignatureAlgorithm.HS512, DatatypeConverter.printBase64Binary(accessTokenKey.getBytes()))
                .compact();
        String refreshToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(refreshExpires)
                .signWith(SignatureAlgorithm.HS512, DatatypeConverter.printBase64Binary(refreshTokenKey.getBytes()))
                .compact();

        String code = RandomStringUtils.randomAlphanumeric(10);

        return new AppToken()
                .setUserId(user.getId())
                .setAppId(app.getId())
                .setAppCode(code)
                .setAccessToken(accessToken.concat(":" + app.getId()))
                .setRefreshToken(refreshToken.concat(":" + app.getId()))
                .setAccessExpires(accessExpires)
                .setRefreshExpires(refreshExpires);
    }

    private Jws<Claims> decryptAuthToken(String key, String token) {
        Integer appId = Integer.parseInt(token.substring(token.lastIndexOf(":") + 1));
        String tokenKey = appService.findById(appId).getSecret() + key;
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.printBase64Binary(tokenKey.getBytes()))
                .parseClaimsJws(token.substring(0, token.lastIndexOf(":")));
    }
}
