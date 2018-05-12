package ru.discloud.auth.repository.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.discloud.auth.domain.UserToken;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class UserTokenRepository {
    private static final String DELIMITER = ":::";
    private static final String USER_TOKEN_KEY = "userToken_device";
    private static final String ACCESS_TOKEN_KEY = "userToken_access";
    private static final String REFRESH_TOKEN_KEY = "userToken_refresh";

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public UserTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public UserToken save(UserToken userToken) {
        Date now = new Date();
        String userTokenDeviceMapKey = USER_TOKEN_KEY + "_" + userToken.getUserId();
        String userAccessKey = ACCESS_TOKEN_KEY + "_" + userToken.getAccessToken();
        String userRefreshKey = REFRESH_TOKEN_KEY + "_" + userToken.getRefreshToken();
        String userDevice = userToken.getUserId() + DELIMITER + userToken.getDeviceId();
        redisTemplate.boundHashOps(userTokenDeviceMapKey).put(userToken.getDeviceId(), userToken.getAccessToken() + DELIMITER + userToken.getRefreshToken());
        redisTemplate.boundValueOps(userAccessKey).set(userDevice, userToken.getAccessExpires().getTime() - now.getTime(), TimeUnit.MILLISECONDS);
        redisTemplate.boundValueOps(userRefreshKey).set(userDevice, userToken.getRefreshExpires().getTime() - now.getTime(), TimeUnit.MILLISECONDS);
        return userToken;
    }

    public Optional<UserTokenDevice> getAccessToken(String accessToken) {
        String userAccessKey = ACCESS_TOKEN_KEY + "_" + accessToken;
        try {
            return Optional.of(parseUserTokenDevice(redisTemplate.boundValueOps(userAccessKey).get()));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public Optional<UserTokenDevice> getRefreshToken(String refreshToken) {
        String userRefreshKey = REFRESH_TOKEN_KEY + "_" + refreshToken;
        try {
            return Optional.of(parseUserTokenDevice(redisTemplate.boundValueOps(userRefreshKey).get()));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public void delete(UserTokenDevice userTokenDevice) {
        String userTokenDeviceMapKey = USER_TOKEN_KEY + "_" + userTokenDevice.getUserId();
        String tokenInfo = (String) redisTemplate.boundHashOps(userTokenDeviceMapKey).get(userTokenDevice.getDeviceId());
        String accessKey = tokenInfo.split(DELIMITER)[0];
        String refreshKey = tokenInfo.split(DELIMITER)[1];
        String userAccessKey = ACCESS_TOKEN_KEY + "_" + accessKey;
        String userRefreshKey = REFRESH_TOKEN_KEY + "_" + refreshKey;
        redisTemplate.delete(userAccessKey);
        redisTemplate.delete(userRefreshKey);
        redisTemplate.boundHashOps(userTokenDeviceMapKey).delete(userTokenDevice.getDeviceId());
    }

    private UserTokenDevice parseUserTokenDevice(String userDevice) {
        Long userId = Long.parseLong(userDevice.split(DELIMITER)[0]);
        String deviceId = userDevice.split(DELIMITER)[1];
        return new UserTokenDevice(userId, deviceId);
    }
}
