package ru.discloud.statistics.interceptor;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ru.discloud.shared.auth.ProxyAuthInterceptor;
import ru.discloud.shared.auth.ProxyUser;
import ru.discloud.statistics.StatisticsApplication;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class TokenProxyAuthInterceptor extends ProxyAuthInterceptor {

  public TokenProxyAuthInterceptor() throws Exception {
    super();
    DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = documentBuilder.parse(StatisticsApplication.class.getClassLoader().getResourceAsStream("auth.xml"));
    NodeList nodeList = document.getElementsByTagName("client");
    Stream<Element> nodeStream = IntStream.range(0, nodeList.getLength()).mapToObj(index -> (Element) nodeList.item(index));
    List<ProxyUser> users = nodeStream
        .map(node -> new ProxyUser()
            .setUsername(node.getElementsByTagName("username").item(0).getTextContent())
            .setPassword(node.getElementsByTagName("password").item(0).getTextContent()))
        .collect(Collectors.toList());
    String secret = "secret";
    setAuthProviderCredentials(users, secret);
  }
}
