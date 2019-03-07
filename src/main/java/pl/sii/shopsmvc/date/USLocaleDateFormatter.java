package pl.sii.shopsmvc.date;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class USLocaleDateFormatter implements Formatter<LocalDate> {
    private static final String US_PATTERN = "MM/dd/yyyy";
    private static final String NORMAL_PATTERN = "dd-MM-yyyy";

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern(getPattern(locale)));
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return DateTimeFormatter.ofPattern(getPattern(locale)).format(object);
    }

    public String getPattern(Locale locale) {
        return isUnitedState(locale) ? US_PATTERN : NORMAL_PATTERN;
    }

    private boolean isUnitedState(Locale locale) {
        return Locale.US.getLanguage().equals(locale.getLanguage());
    }
}
