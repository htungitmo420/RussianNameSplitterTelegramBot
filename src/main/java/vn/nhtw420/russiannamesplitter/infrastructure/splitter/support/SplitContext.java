package vn.nhtw420.russiannamesplitter.infrastructure.splitter.support;

import java.util.ArrayList;
import java.util.List;

public class SplitContext {
    private final List<String> tokens;

    private String surname;
    private String givenName;
    private String patronymic;
    private double confidence;

    public SplitContext(List<String> tokens) {
        this.tokens = tokens == null ? List.of() : List.copyOf(tokens);
        this.confidence = 0.0;
    }

    public List<String> tokens() { return tokens; }

    public String surname() { return surname; }
    public String givenName() { return givenName; }
    public String patronymic() { return patronymic; }
    public double confidence() { return confidence; }

    public void setSurname(String surname) { this.surname = surname; }
    public void setGivenName(String givenName) { this.givenName = givenName; }
    public void setPatronymic(String patronymic) { this.patronymic = patronymic; }

    public void addConfidence(double delta) {
        this.confidence = clamp01(this.confidence + delta);
    }

    public void setConfidence(double value) {
        this.confidence = clamp01(value);
    }

    public boolean hasAll() {
        return notBlank(surname) && notBlank(givenName) && notBlank(patronymic);
    }

    public boolean hasAny() {
        return notBlank(surname) || notBlank(givenName) || notBlank(patronymic);
    }

    public static List<String> tokenize(String normalizedInput) {
        if (normalizedInput == null || normalizedInput.isBlank()) return List.of();
        String[] parts = normalizedInput.trim().split("\\s+");
        List<String> out = new ArrayList<>(parts.length);
        for (String p : parts) {
            String t = p.trim();
            if (!t.isEmpty()) out.add(t);
        }
        return out;
    }

    private static boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    private static double clamp01(double x) {
        if (x < 0.0) return 0.0;
        return Math.min(x, 0.95); // intentionally cap to avoid fake certainty
    }
}
