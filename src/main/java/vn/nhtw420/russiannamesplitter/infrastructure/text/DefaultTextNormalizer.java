package vn.nhtw420.russiannamesplitter.infrastructure.text;

import org.springframework.stereotype.Component;
import vn.nhtw420.russiannamesplitter.application.port.TextNormalizerPort;

@Component
public class DefaultTextNormalizer implements TextNormalizerPort {

    @Override
    public String normalizeNameInput(String raw) {
        if (raw == null) return "";

        // trim + normalize spaces
        String s = raw.trim()
                .replace('\u00A0', ' ')         // non-breaking space
                .replaceAll("[\\t\\n\\r]+", " ")
                .replaceAll("\\s{2,}", " ");

        // remove surrounding quotes
        s = stripQuotes(s);

        // unify separators like commas/semicolon into space
        s = s.replace(',', ' ')
                .replace(';', ' ')
                .replaceAll("\\s{2,}", " ")
                .trim();

        return s;
    }

    private static String stripQuotes(String s) {
        if (s == null || s.length() < 2) return s;
        char first = s.charAt(0);
        char last = s.charAt(s.length() - 1);
        if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
            return s.substring(1, s.length() - 1).trim();
        }
        return s;
    }
}
