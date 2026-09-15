package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FormataData {
    public static String format(LocalDate data, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        return data.format(formatter);
    }
}
