package co.wethinkcode.healthsafe;

import io.javalin.Javalin;
import co.wethinkcode.healthsafe.mq.MqConfig;
public class EquipmentAlertServiceApp {
    private static MqConfig mqConfig = new MqConfig();

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7034);

        app.get("/health", ctx -> ctx.result("OK"));
        mqConfig.startListening();

        // TODO (Uses a Queue to guarantee delivery of critical medical equipment failure alerts.)
        // Mechanism: ActiveMQ Queue (guaranteed delivery)

    }
}

// MQ TODO: consumes ActiveMQ queue MqConfig.QUEUE at MqConfig.BROKER_URL (see co.wethinkcode.healthsafe.mq.MqConfig)
// Producer: ward-service publishes here when it detects an equipment failure on one of its wards.
