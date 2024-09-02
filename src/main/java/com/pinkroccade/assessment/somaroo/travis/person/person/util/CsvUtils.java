package com.pinkroccade.assessment.somaroo.travis.person.person.util;

import com.opencsv.CSVWriter;
import com.pinkroccade.assessment.somaroo.travis.person.person.model.Person;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Base64;
import java.util.List;

public class CsvUtils {

    public static String generateCsv(List<Person> persons) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream))) {
            writer.writeNext(new String[]{"ID", "Name", "BirthDate", "Partner", "NumberOfChildren"});
            for (Person person : persons) {
                writer.writeNext(new String[]{
                        String.valueOf(person.getId()),
                        person.getName(),
                        person.getBirthDate().toString(),
                        person.getPartner() != null ? person.getPartner().getName() : "None",
                        String.valueOf(person.getChildren().size())
                });
            }
        }
        return outputStream.toString();
    }

    public static String encodeToBase64(String content) {
        return Base64.getEncoder().encodeToString(content.getBytes());
    }

}
