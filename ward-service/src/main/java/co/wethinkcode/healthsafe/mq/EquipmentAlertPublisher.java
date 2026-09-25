package co.wethinkcode.healthsafe.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.JMSException;
import javax.jms.*;

public class EquipmentAlertPublisher {

    public void sendAlert(String wardId, String equipmentDescription) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(MqConfig.BROKER_URL);

        try (Connection connection = connectionFactory.createConnection()) {
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(MqConfig.QUEUE);

            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            String payload = String.format(
                    "{\"wardId\":\"%s\",\"equipment\":\"%s\"}", wardId, equipmentDescription
            );
            TextMessage message = session.createTextMessage(payload);
            producer.send(message);

            System.out.println("Sent equipment failure alert: " + message.getText());

        } catch (JMSException error) {
            error.printStackTrace();
            throw error;
        }
    }
}