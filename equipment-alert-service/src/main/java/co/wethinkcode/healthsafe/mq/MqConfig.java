package co.wethinkcode.healthsafe.mq;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Shared by every producer/consumer service that talks to the "equipment-failure-queue"
 * ActiveMQ queue. Duplicated into each participating service's own source tree,
 * since these are independent Maven projects with no shared parent pom.
 */
public final class MqConfig {

    public static final String BROKER_URL = "tcp://localhost:61616";
    public static final String QUEUE = "equipment-failure-queue";

    public MqConfig() {
    }

    public void startListening() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            Destination destination = session.createQueue(QUEUE);

            MessageConsumer consumer = session.createConsumer(destination);

            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        if (message instanceof TextMessage) {
                            TextMessage textMessage = (TextMessage) message;
                            System.out.println("EQUIPMENT FAILURE ALERT: " + textMessage.getText());
                            message.acknowledge();
                        }
                    } catch (JMSException error) {
                        System.err.println("Error processing equipment alert: " + error.getMessage());
                    }
                }
            });

            System.out.println("Equipment alert consumer is listening (guaranteed delivery).");

        } catch (Exception error) {
            System.err.println("Error setting up equipment alert consumer: " + error.getMessage());
        }
    }
}

