package ru.discloud.user.mail;

import ru.discloud.user.integration.mailgun.SendMessageRequest;

public class UserSignupMessage extends SendMessageRequest {
    public UserSignupMessage(String to) {
        this.from = "noreply@discloud.ru";
        this.to = to;
        this.subject = "Регистрация на discloud.ru";
        this.text = "Спасибо за оказанное вами доверие";
    }
}
