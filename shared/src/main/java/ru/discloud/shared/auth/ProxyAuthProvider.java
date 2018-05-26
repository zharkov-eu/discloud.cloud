package ru.discloud.shared.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Setter;

import javax.xml.bind.DatatypeConverter;
import java.util.*;

public class ProxyAuthProvider {
  private final String delimiter = ":::";
  private final int LRU_MAX_CAPACITY = 100;
  private Map<String, Date> tokensLruCache = new LinkedHashMap<String, Date>(LRU_MAX_CAPACITY, 0.75f, true) {
    @Override
    protected boolean removeEldestEntry(Map.Entry<String, Date> eldest) {
      return size() > LRU_MAX_CAPACITY;
    }
  };
  @Setter
  private List<ProxyUser> users;
  @Setter
  private String secret;

  ProxyAuthProvider() {
    this.users = new ArrayList<>();
    this.secret = "";
  }

  ProxyAuthProvider(List<ProxyUser> users, String secret) {
    this.users = users;
    this.secret = secret;
  }

  Boolean checkAccessToken(String token) throws Exception {
    if (token.split(delimiter).length != 2) throw new ProxyTokenInvalidException();
    String clientUsername = token.split(delimiter)[0];
    String accessToken = token.split(delimiter)[1];
    Date now = new Date();

    ProxyUser user = users.stream()
        .filter(it -> it.getUsername().equals(clientUsername))
        .findFirst().orElseThrow(ProxyBadCredentialsException::new);

    Date cachedExpirationDate = tokensLruCache.get(accessToken);
    if (cachedExpirationDate != null) return now.before(cachedExpirationDate);

    try {
      String sign = user.getPassword() + secret;
      Jws<Claims> jws = Jwts.parser()
          .setSigningKey(DatatypeConverter.printBase64Binary(sign.getBytes()))
          .parseClaimsJws(accessToken);
      if (new Date().before(jws.getBody().getExpiration())) {
        tokensLruCache.put(accessToken, jws.getBody().getExpiration());
      }
      return now.before(jws.getBody().getExpiration());
    } catch (Exception ignored) {
      throw new ProxyTokenInvalidException();
    }
  }

  ProxyAuthToken createAccessToken(ProxyUser user) {
    Date expirationDate = new Date(System.currentTimeMillis() + (60 * 60 * 1000));
    Date now = new Date();
    String sign = user.getPassword() + secret;
    String accessToken = Jwts.builder()
        .setSubject(user.getUsername())
        .setExpiration(expirationDate)
        .signWith(SignatureAlgorithm.HS512, DatatypeConverter.printBase64Binary(sign.getBytes()))
        .compact();
    return new ProxyAuthToken()
        .setExpire(expirationDate.getTime() - now.getTime())
        .setToken(user.getUsername() + delimiter + accessToken);
  }

  Optional<ProxyUser> userByBasicAuthorization(String authorization) {
    if (authorization == null || !authorization.startsWith("Basic")) return Optional.empty();
    String base64Credentials = authorization.substring("Basic".length()).trim();
    String[] credentials = new String(DatatypeConverter.parseBase64Binary(base64Credentials)).split(":");
    return users.stream()
        .filter(it -> it.getUsername().equals(credentials[0]) && it.getPassword().equals(credentials[1]))
        .findFirst();
  }
}