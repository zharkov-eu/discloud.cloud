package ru.discloud.shared.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.xml.bind.DatatypeConverter;
import java.util.*;

public class AuthInterceptor<T extends AuthClient> {
    private final int LRU_MAX_CAPACITY = 100;
    private Map<String, Date> tokensLruCache = new LinkedHashMap<String, Date>(LRU_MAX_CAPACITY, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Date> eldest) {
            return size() > LRU_MAX_CAPACITY;
        }
    };
    private final String delimiter = ":::";
    private List<T> clients;
    private String secret;

    public AuthInterceptor(List<T> clients, String secret) {
        this.clients = clients;
        this.secret = secret;
    }

    public Boolean checkAccessToken(String token) throws Exception {
        if (token.split(delimiter).length != 2) throw new TokenInvalidException();
        String clientUsername = token.split(delimiter)[0];
        String accessToken = token.split(delimiter)[1];
        Date now = new Date();

        AuthClient client = clients.stream()
                .filter(it -> it.getUsername().equals(clientUsername))
                .findFirst().orElseThrow(AuthClientNotFoundException::new);

        Date cachedExpirationDate = tokensLruCache.get(accessToken);
        if (cachedExpirationDate != null) return now.before(cachedExpirationDate);

        try {
            String sign = client.getPassword() + secret;
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(DatatypeConverter.printBase64Binary(sign.getBytes()))
                    .parseClaimsJws(accessToken);
            if (new Date().before(jws.getBody().getExpiration())) {
                tokensLruCache.put(accessToken, jws.getBody().getExpiration());
            }
            return now.before(jws.getBody().getExpiration());
        } catch (Exception ignored) {
            throw new TokenInvalidException();
        }
    }

    public AuthToken createAccessToken(AuthClient client) {
        Date expirationDate = new Date(System.currentTimeMillis() + (60 * 60 * 1000));
        Date now = new Date();
        String sign = client.getPassword() + secret;
        String accessToken = Jwts.builder()
                .setSubject(client.getUsername())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, DatatypeConverter.printBase64Binary(sign.getBytes()))
                .compact();
        return new AuthToken()
                .setExpire(expirationDate.getTime() - now.getTime())
                .setToken(client.getUsername() + delimiter + accessToken);
    }

    public Optional<T> clientByBasicAuthorization(String authorization) {
        if (authorization == null || !authorization.startsWith("Basic")) return Optional.empty();
        String base64Credentials = authorization.substring("Basic".length()).trim();
        String[] credentials = new String(DatatypeConverter.parseBase64Binary(base64Credentials)).split(":");
        return clients.stream()
                .filter(it -> it.getUsername().equals(credentials[0]) && it.getPassword().equals(credentials[1]))
                .findFirst();
    }
}