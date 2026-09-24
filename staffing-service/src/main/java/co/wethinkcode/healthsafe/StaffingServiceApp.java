package co.wethinkcode.healthsafe;

import io.javalin.Javalin;

public class StaffingServiceApp {
    private final Javalin server;
    private final StaffingService staffingService;


    public StaffingServiceApp(){
        this.staffingService = new StaffingService();
        this.server = Javalin.create();
        this.server.get("/health", ctx -> ctx.result("OK"));
        this.server.get("/staffing/{id}",ctx -> {
            StaffingResponse response = staffingService.getStaffingForWard(
                    ctx.pathParam("id")
            );
            ctx.json(response);
                });
    }

    public Javalin start(){
       return this.server.start(7033);
    }

    public Javalin stop(){
        return this.server.stop();
    }

    public static void main(String[] args) {
        StaffingServiceApp app = new StaffingServiceApp();
        app.start();
//        Javalin app = Javalin.create().start(7033);

        //app.get("/health", ctx -> ctx.result("OK"));

        // TODO (Provides on-call schedules for doctors based on ward and status.)
        // Add domain endpoints for staffing-service here.
    }
}

// MQ TODO: publishes to ActiveMQ topic MqConfig.TOPIC at MqConfig.BROKER_URL (see co.wethinkcode.healthsafe.mq.MqConfig)
