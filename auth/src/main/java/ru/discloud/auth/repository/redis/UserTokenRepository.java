package ru.discloud.auth.repository.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.discloud.auth.domain.UserToken;

import java.util.Date;

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
        redisTemplate.boundValueOps(userAccessKey).set(userDevice, userToken.getAccessExpires().getTime() - now.getTime());
        redisTemplate.boundValueOps(userRefreshKey).set(userDevice, userToken.getRefreshExpires().getTime() - now.getTime());
        return userToken;
    }

    public UserTokenDevice getAccessToken(String accessToken) {
        String userAccessKey = ACCESS_TOKEN_KEY + "_" + accessToken;
        return parseUserTokenDevice(redisTemplate.boundValueOps(userAccessKey).get());
    }

    public UserTokenDevice getRefreshToken(String refreshToken) {
        String userRefreshKey = REFRESH_TOKEN_KEY + "_" + refreshToken;
        return parseUserTokenDevice(redisTemplate.boundValueOps(userRefreshKey).get());
    }

    public void delete(UserToken userToken) {
        String userTokenDeviceMapKey = USER_TOKEN_KEY + "_" + userToken.getUserId();
        String tokenInfo = (String) redisTemplate.boundHashOps(userTokenDeviceMapKey).get(userToken.getDeviceId());
        String accessKey = tokenInfo.split(DELIMITER)[0];
        String refreshKey = tokenInfo.split(DELIMITER)[1];
        String userAccessKey = ACCESS_TOKEN_KEY + "_" + accessKey;
        String userRefreshKey = REFRESH_TOKEN_KEY + "_" + refreshKey;
        redisTemplate.delete(userAccessKey);
        redisTemplate.delete(userRefreshKey);
        redisTemplate.boundHashOps(userTokenDeviceMapKey).delete(userToken.getDeviceId());
    }

    private UserTokenDevice parseUserTokenDevice(String userDevice) {
        Integer userId = Integer.parseInt(userDevice.split(DELIMITER)[0]);
        String deviceId = userDevice.split(DELIMITER)[1];
        return new UserTokenDevice(userId, deviceId);
    }
}
