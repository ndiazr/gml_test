package com.gml.clients.util;

import com.gml.clients.entity.Client;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class CsvGenerator {
    public static ByteArrayInputStream clientsToCSV(List<Client> clients) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (Client client : clients) {
                List<String> data = Arrays.asList(
                        String.valueOf(client.getId()),
                        client.getSharedKey(),
                        client.getName(),
                        client.getEmail(),
                        client.getPhone(),
                        String.valueOf(client.getStartDate()),
                        String.valueOf(client.getEndDate())
                );
                csvPrinter.printRecord(data);
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}
