package com.app.e_library.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class CsvFileReader {

    public List<CSVRecord> readFile(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVParser csvParser =
                    CSVFormat.newFormat(';')
                            .withFirstRecordAsHeader()
                            .withIgnoreEmptyLines(true)
                            .withTrim()
                            .parse(reader);

            return csvParser.getRecords();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
