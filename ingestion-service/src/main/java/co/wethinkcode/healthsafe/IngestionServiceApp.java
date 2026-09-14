package co.wethinkcode.healthsafe;

import io.javalin.Javalin;

import java.util.ArrayList;


public class IngestionServiceApp {

    public static void main(String[] args) throws Exception {
        Javalin app = Javalin.create().start(7030);
        String filePath = "wards-outdated.csv";
        ArrayList<WardRecordModel> cleaned = CsvCleaner.getCleanedWards();

        app.get("/health", ctx -> ctx.result("OK"));
            app.get("/wards", ctx -> {
                ctx.json(cleaned);
        });

        // TODO: read and clean src/main/resources/wards-outdated.csv (wards, wings, specialist departments data —
        // trim whitespace, fix casing, normalize dates/booleans) and expose the
        // cleaned records here for the other services to consume.




    }
}


