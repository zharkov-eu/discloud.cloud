package ru.discloud.statistics.queue;

import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RedisMessageSubscriber {
    public static List<String> messageList = new ArrayList<>();

    public void onMessage(final Message message, final byte[] pattern) {
        messageList.add(message.toString());
        System.out.println("Message received: " + new String(message.getBody()));
    }
}
