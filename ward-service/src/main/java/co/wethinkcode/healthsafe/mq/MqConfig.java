package co.wethinkcode.healthsafe.mq;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
/**
 * Shared by every producer/consumer service that talks to the ActiveMQ broker.
 * Duplicated into each participating service's own source tree, since these are
 * independent Maven projects with no shared parent pom.
 *
 * ward-service is a consumer of TOPIC (staffing updates) and a producer on QUEUE
 * (equipment failures detected on its wards).
 */
public final class MqConfig {

    public static final String BROKER_URL = "tcp://localhost:61616";
    public static final String TOPIC = "staffing-events-topic";
    public static final String QUEUE = "equipment-failure-queue";

    public MqConfig() {
    }

        public void startListening() {

            try {
                ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
                Connection connection = connectionFactory.createConnection();
                connection.start();

                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                Destination destination1 = session.createTopic(TOPIC);
                // Destination destination = session.createTopic(QUEUE);

                MessageConsumer consumer = session.createConsumer(destination1);
                // MessageConsumer consumer2 = session.createConsumer(destination);

                consumer.setMessageListener(new MessageListener() {
                    @Override
                    public void onMessage(Message message) {
                        try {
                            if (message instanceof TextMessage) {
                                TextMessage textMessage = (TextMessage) message;
                                System.out.println("Asynchronously received: " + textMessage.getText());
                            }
                        } catch (JMSException error) {
                            System.err.println("Error processing message: " + error.getMessage());
                        }
                    }
                });

//                System.out.println("Consumer is listening asynchronously. Press Enter to exit...");
//                System.in.read();

//                consumer.close();
//                session.close();
//                connection.close();

            } catch (Exception error) {
                System.err.println("Error processing message: " + error.getMessage());
            }
        }
}
