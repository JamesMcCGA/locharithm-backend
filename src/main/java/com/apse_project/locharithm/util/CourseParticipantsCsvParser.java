package com.apse_project.locharithm.util;

import com.apse_project.locharithm.domain.User;
import com.apse_project.locharithm.domain.UserRole;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class CourseParticipantsCsvParser {

    public List<User> parse(MultipartFile file) {
        List<User> participants = new ArrayList<>();

        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                new BOMInputStream(file.getInputStream()), // This handles BOM
                                StandardCharsets.UTF_8));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withHeader()
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim())
        ) {
            for (CSVRecord csvRecord : csvParser) {
                String firstName = csvRecord.get("First name");
                String lastName = csvRecord.get("Last name");
                String email = csvRecord.get("Email address");

                User user = new User();
                user.setUserName(firstName + " " + lastName);
                user.setUserEmail(email);
                user.setRole(UserRole.STUDENT);
                participants.add(user);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV: " + e.getMessage(), e);
        }

        return participants;
    }
}
