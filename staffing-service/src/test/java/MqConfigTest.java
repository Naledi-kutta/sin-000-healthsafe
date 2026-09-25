package co.wethinkcode.healthsafe.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.jupiter.api.Test;

import javax.jms.*;

import static org.junit.jupiter.api.Assertions.*;

class MqConfigTest {

    @Test
    void shouldPublishStaffingMessageToTopic() throws Exception {

        MqConfig mqConfig = new MqConfig();

        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory(MqConfig.BROKER_URL);

        try (Connection connection = connectionFactory.createConnection()) {

            connection.start();

            Session session =
                    connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination topic =
                    session.createTopic(MqConfig.TOPIC);

            MessageConsumer consumer =
                    session.createConsumer(topic);

            mqConfig.sendMessage("W-01", 3);

            Message message = consumer.receive(3000);

            assertNotNull(message);

            assertTrue(message instanceof TextMessage);

            TextMessage textMessage = (TextMessage) message;

            String body = textMessage.getText();

            assertTrue(body.contains("\"wardId\":\"W-01\""));
            assertTrue(body.contains("\"alertLevel\":3"));
        }
    }
}