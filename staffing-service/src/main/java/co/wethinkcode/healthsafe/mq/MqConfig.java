package co.wethinkcode.healthsafe.mq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Shared by every producer/consumer service that talks to the "staffing-events-topic"
 * ActiveMQ topic. Duplicated into each participating service's own source tree,
 * since these are independent Maven projects with no shared parent pom.
 */
public final class MqConfig {

    public static final String BROKER_URL = "tcp://localhost:61616";
    public static final String TOPIC = "staffing-events-topic";

    public MqConfig() {
    }

    public void sendMessage(String wardId,int alertLevel) {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

            try (
                    Connection connection = connectionFactory.createConnection()) {
                connection.start();

                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                Destination destination = session.createTopic(TOPIC);

                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.PERSISTENT);

                String payload = String.format( "{\"wardId\":\"%s\",\"alertLevel\":%d}", wardId, alertLevel);

                TextMessage message = session.createTextMessage(payload);
                producer.send(message);

                System.out.println("Sent messsage: " + message.getText());

            } catch (JMSException error) {
                error.printStackTrace();
            }
        }
}

