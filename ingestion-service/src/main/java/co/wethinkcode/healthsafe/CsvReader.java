package co.wethinkcode.healthsafe;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.CSVReader;
import org.eclipse.jetty.util.IO;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    public static List<WardRecordModel> read(String filePath) throws IOException {

        try (FileReader fileReader = new FileReader(filePath)) {
            return new CsvToBeanBuilder<WardRecordModel>(fileReader)
                    .withType((WardRecordModel.class))
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse()
                    ;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filePath = "ingestion-service/src/main/resources/wards-outdated.csv";

        try {
            List<WardRecordModel> records = read(filePath);
            //record = row in csv
            for (WardRecordModel record : records) {
                System.out.println("---");
                System.out.println("Ward ID: " + record.getWardId().toString());
                System.out.println("Wing : " + record.getWing().toString());
                System.out.println("Department: " + record.getDepartment().toString());
                System.out.println("Beds Available: " + record.getBedsAvailable().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}