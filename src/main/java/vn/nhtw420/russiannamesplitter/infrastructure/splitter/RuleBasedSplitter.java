package vn.nhtw420.russiannamesplitter.infrastructure.splitter;

import org.springframework.stereotype.Component;
import vn.nhtw420.russiannamesplitter.application.port.FioSplitterPort;
import vn.nhtw420.russiannamesplitter.infrastructure.splitter.support.NameHeuristics;

import java.util.ArrayList;
import java.util.List;

@Component
public class RuleBasedSplitter implements FioSplitterPort {

    @Override
    public SplitResult split(String normalizedInput) {
        if (normalizedInput == null || normalizedInput.isBlank()) {
            return new SplitResult(null, null, null, 0.0);
        }

        List<String> tokens = tokenize(normalizedInput);
        if (tokens.isEmpty()) {
            return new SplitResult(null, null, null, 0.0);
        }

        // 3+ tokens -> mostly "Surname Given Patronymic"
        if (tokens.size() >= 3) {
            String surname = tokens.get(0);
            String given = tokens.get(1);
            String patronymic = tokens.get(2);

            // if there are extra tokens, assume compound surname and last two are given+patronymic
            if (tokens.size() > 3) {
                surname = String.join(" ", tokens.subList(0, tokens.size() - 2));
                given = tokens.get(tokens.size() - 2);
                patronymic = tokens.get(tokens.size() - 1);
            }

            double confidence = estimateConfidence3(surname, given, patronymic);
            return new SplitResult(surname, given, patronymic, confidence);
        }

        // 2 tokens
        if (tokens.size() == 2) {
            String a = tokens.get(0);
            String b = tokens.get(1);

            // If b looks like patronymic => either:
            //  (GivenName + Patronymic) OR (Surname + Patronymic)
            if (NameHeuristics.looksLikePatronymic(b)) {
                if (NameHeuristics.looksLikeSurname(a)) {
                    // Surname + Patronymic
                    return new SplitResult(a, null, b, 0.65);
                }
                // GivenName + Patronymic
                return new SplitResult(null, a, b, 0.65);
            }

            // Default: Surname + GivenName
            return new SplitResult(a, b, null, 0.70);
        }

        // 1 token: decide patronymic / surname / givenName (in that order)
        String only = tokens.get(0);

        if (NameHeuristics.looksLikePatronymic(only)) {
            return new SplitResult(null, null, only, 0.55);
        }

        if (NameHeuristics.looksLikeSurname(only)) {
            return new SplitResult(only, null, null, 0.55);
        }

        // fallback: treat as given name (e.g. "Наталия")
        return new SplitResult(null, only, null, 0.40);
    }

    private static List<String> tokenize(String s) {
        String[] parts = s.trim().split("\\s+");
        List<String> tokens = new ArrayList<>(parts.length);
        for (String p : parts) {
            String t = p.trim();
            if (!t.isEmpty()) tokens.add(t);
        }
        return tokens;
    }

    private static double estimateConfidence3(String surname, String given, String patronymic) {
        double c = 0.75;

        if (NameHeuristics.looksLikePatronymic(patronymic)) c += 0.15;
        if (NameHeuristics.looksLikeSurname(surname)) c += 0.05;
        if (looksCyrillicWord(given)) c += 0.05;

        if (c > 0.95) c = 0.95;
        return c;
    }

    private static boolean looksCyrillicWord(String s) {
        if (s == null || s.isBlank()) return false;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch >= '\u0400' && ch <= '\u04FF') { // Cyrillic block includes Ёё
                return true;
            }
        }
        return false;
    }
}
