package mobile.che.com.oddymobstar.chemobile.service.util;

import android.util.Log;

import java.util.LinkedHashMap;

import factory.MessageFactory;
import message.CheMessage;
import mobile.che.com.oddymobstar.chemobile.service.CheServiceSocket;
import util.Tags;

/**
 * Created by timmytime on 15/02/16.
 */
public class CheMessageQueue {


    private final LinkedHashMap<String, CheMessage> messageQueue = new LinkedHashMap<>();
    private final CheServiceSocket cheServiceSocket;

    private Object lock = new Object();

    private String lastSentKey = "";

    public CheMessageQueue(CheServiceSocket cheServiceSocket) {
        this.cheServiceSocket = cheServiceSocket;

    }


    public synchronized void addMessage(CheMessage cheMessage) {
        synchronized (lock) {
            if (cheMessage.containsMessage(Tags.CHE_ACKNOWLEDGE)) {
                message.Acknowledge cheAck = (message.Acknowledge) MessageFactory.getCheMessage(cheMessage.getMessage(Tags.CHE_ACKNOWLEDGE).toString(), Tags.CHE_ACKNOWLEDGE);
                messageQueue.put(cheAck.getCheAckId(), cheMessage);

            } else {
                messageQueue.put(cheMessage.getMessage(Tags.ACKNOWLEDGE).getKey(), cheMessage);
            }

            String nextKey = messageQueue.keySet().iterator().next();
            if (!lastSentKey.equals(nextKey)) {
                lastSentKey = nextKey;
                cheServiceSocket.write(messageQueue.get(lastSentKey));
            }
        }
    }


    public synchronized void receiveAck(String ack) {
        synchronized (lock) {
            messageQueue.remove(ack);
            lastSentKey = "";
            try {
                if (!messageQueue.isEmpty()) {
                    cheServiceSocket.write(messageQueue.get(messageQueue.keySet().iterator().next()));
                }
            } catch (Exception e) {
                Log.d("receive ack", "receive error " + e.getMessage());
            }
        }

    }

    public void reConnect() {
        try {
            cheServiceSocket.write(messageQueue.get(messageQueue.keySet().iterator().next()));
        } catch (Exception e) {
            Log.d("Queue exception", "queue error is " + e.getMessage());
        }
    }

    public void clear() {
        messageQueue.clear();
    }
}
