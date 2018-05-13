package ru.discloud.auth.repository.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.discloud.auth.domain.AppToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class AppTokenRepository {
    private static final String DELIMITER = ":::";
    private static final String APP_TOKEN_KEY = "appToken_device";
    private static final String APP_CODE_KEY = "appToken_code";
    private static final String ACCESS_TOKEN_KEY = "appToken_access";
    private static final String REFRESH_TOKEN_KEY = "appToken_refresh";
    private static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public AppTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public AppToken save(AppToken appToken) {
        Date now = new Date();
        String appCodeKey = APP_CODE_KEY + "_" + appToken.getAppId() + "_" + appToken.getAppCode();
        String userTokenDeviceMapKey = APP_TOKEN_KEY + "_" + appToken.getAppId() + "_" + appToken.getUserId();
        String userAccessKey = ACCESS_TOKEN_KEY + "_" + appToken.getAccessToken();
        String userRefreshKey = REFRESH_TOKEN_KEY + "_" + appToken.getRefreshToken();
        String appUserDevice = appToken.getAppId() + DELIMITER + appToken.getUserId() + DELIMITER + appToken.getDeviceId();
        redisTemplate.boundHashOps(userTokenDeviceMapKey).put(appToken.getDeviceId(), appToken.getAccessToken() + DELIMITER + appToken.getRefreshToken());
        redisTemplate.boundValueOps(userAccessKey).set(appUserDevice, appToken.getAccessExpires().getTime() - now.getTime(), TimeUnit.MILLISECONDS);
        redisTemplate.boundValueOps(userRefreshKey).set(appUserDevice, appToken.getRefreshExpires().getTime() - now.getTime(), TimeUnit.MILLISECONDS);
        return appToken;
    }

    public Optional<AppTokenDevice> getAccessToken(String accessToken) {
        String userAccessKey = ACCESS_TOKEN_KEY + "_" + accessToken;
        try {
            return Optional.of(parseAppUserDevice(redisTemplate.boundValueOps(userAccessKey).get()));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public Optional<AppTokenDevice> getRefreshToken(String refreshToken) {
        String userRefreshKey = REFRESH_TOKEN_KEY + "_" + refreshToken;
        try {
            return Optional.of(parseAppUserDevice(redisTemplate.boundValueOps(userRefreshKey).get()));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public Optional<AppToken> getTokenByAppCode(Integer appId, String appCode) {
        String appCodeKey = APP_CODE_KEY + "_" + appId + "_" + appCode;
        try {
            return Optional.of(parseAppCodeToken(redisTemplate.boundValueOps(appCodeKey).get()));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public void delete(AppTokenDevice appTokenDevice) {
        String userTokenDeviceMapKey = APP_TOKEN_KEY + "_" + appTokenDevice.getAppId() + "_" + appTokenDevice.getUserId();
        String tokenInfo = (String) redisTemplate.boundHashOps(userTokenDeviceMapKey).get(appTokenDevice.getDeviceId());
        String accessKey = tokenInfo.split(DELIMITER)[0];
        String refreshKey = tokenInfo.split(DELIMITER)[1];
        String userAccessKey = ACCESS_TOKEN_KEY + "_" + accessKey;
        String userRefreshKey = REFRESH_TOKEN_KEY + "_" + refreshKey;
        redisTemplate.delete(userAccessKey);
        redisTemplate.delete(userRefreshKey);
        redisTemplate.boundHashOps(userTokenDeviceMapKey).delete(appTokenDevice.getDeviceId());
    }

    public void deleteAppCode(Integer appId, String appCode) {
        String appCodeKey = APP_CODE_KEY + "_" + appId + "_" + appCode;
        redisTemplate.delete(appCodeKey);
    }

    private AppToken parseAppCodeToken(String appCodeToken) throws ParseException {
        String accessToken = appCodeToken.split(DELIMITER)[0];
        Date accessExpire = ISO_DATE_FORMAT.parse(appCodeToken.split(DELIMITER)[1]);
        String refreshToken = appCodeToken.split(DELIMITER)[2];
        Date refreshExpire = ISO_DATE_FORMAT.parse(appCodeToken.split(DELIMITER)[3]);
        return new AppToken()
                .setAccessToken(accessToken)
                .setAccessExpires(accessExpire)
                .setRefreshToken(refreshToken)
                .setRefreshExpires(refreshExpire);
    }

    private AppTokenDevice parseAppUserDevice(String appUserDevice) {
        Integer appId = Integer.parseInt(appUserDevice.split(DELIMITER)[0]);
        Long userId = Long.parseLong(appUserDevice.split(DELIMITER)[1]);
        String deviceId = appUserDevice.split(DELIMITER)[2];
        return new AppTokenDevice(appId, userId, deviceId);
    }
}
