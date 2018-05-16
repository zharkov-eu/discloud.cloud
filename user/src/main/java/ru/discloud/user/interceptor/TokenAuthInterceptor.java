package ru.discloud.user.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ru.discloud.shared.auth.AuthInterceptor;
import ru.discloud.shared.auth.AuthToken;
import ru.discloud.user.UserApplication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class TokenAuthInterceptor extends HandlerInterceptorAdapter {
    private AuthInterceptor<TokenAuthClient> authInterceptor;

    public TokenAuthInterceptor() throws Exception {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(UserApplication.class.getClassLoader().getResourceAsStream("auth.xml"));
        NodeList nodeList = document.getElementsByTagName("client");
        Stream<Element> nodeStream = IntStream.range(0, nodeList.getLength()).mapToObj(index -> (Element) nodeList.item(index));
        List<TokenAuthClient> clients = nodeStream
                .map(node -> new TokenAuthClient()
                        .setUsername(node.getElementsByTagName("username").item(0).getTextContent())
                        .setPassword(node.getElementsByTagName("password").item(0).getTextContent()))
                .collect(Collectors.toList());
        String secret = "secret";
        authInterceptor = new AuthInterceptor<>(clients, secret);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Basic")) {
            authInterceptor.clientByBasicAuthorization(authorization)
                    .ifPresent(client -> {
                        AuthToken token = authInterceptor.createAccessToken(client);
                        response.setHeader("X-Auth-Token", token.getToken());
                        response.setHeader("X-Auth-Expire", token.getExpire().toString());
                    });
            if (response.getHeader("X-Auth-Token") != null) return true;
        }
        if (authorization != null && authorization.startsWith("Bearer")) {
            if (authInterceptor.checkAccessToken(authorization.substring("Bearer".length()).trim())) return true;
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setHeader("Content-Type", "application/json");
        response.getWriter().write("{\"Success\": false}");
        return false;
    }
}
