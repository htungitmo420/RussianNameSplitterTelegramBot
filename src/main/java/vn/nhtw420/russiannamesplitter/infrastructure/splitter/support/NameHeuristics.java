package vn.nhtw420.russiannamesplitter.infrastructure.splitter.support;

import java.util.Locale;

public final class NameHeuristics {

    private NameHeuristics() {}

    public static boolean looksLikePatronymic(String token) {
        if (token == null || token.isBlank()) return false;
        String x = normalize(token);

        // Common Russian patronymic endings (+ optional)
        return endsWithAny(x,
                "вич","евич","ович",
                "вна","евна","овна",
                "ична","инична",
                "оглы","кызы"
        );
    }

    public static boolean looksLikeSurname(String token) {
        if (token == null || token.isBlank()) return false;
        String x = normalize(token);
        int len = x.length();
        if (len < 3) return false;

        // Strong female surname endings (avoid false positive like "вова")
        if (len >= 5 && (x.endsWith("ова") || x.endsWith("ева"))) return true;

        // Strong adjective-like surname endings
        if (len >= 6 && (x.endsWith("ская") || x.endsWith("цкая"))) return true;
        if (len >= 6 && (x.endsWith("ский") || x.endsWith("ской") || x.endsWith("цкий"))) return true;

        // Common surname endings (male/unisex)
        if (len >= 4 && endsWithAny(x, "ов","ев","ин","ын")) return true;

        // Common regional endings
        if (len >= 4 && endsWithAny(x, "енко","ко","чук","юк","як","ук")) return true;

        // Some adjectival/plural surnames
        if (len >= 4 && endsWithAny(x, "ых", "их")) return true;
        if (len >= 4 && endsWithAny(x, "ого", "его")) return true;

        return false;
    }

    private static boolean endsWithAny(String s, String... suffixes) {
        for (String suf : suffixes) {
            if (s.endsWith(suf)) return true;
        }
        return false;
    }

    private static String normalize(String s) {
        String t = s.trim().toLowerCase(Locale.ROOT);

        // unify ё -> е
        t = t.replace('ё', 'е');

        // remove punctuation/hyphens
        t = t.replace("-", "");

        // remove weird spaces
        t = t.replace('\u00A0', ' ')
                .replaceAll("\\s+", "");

        return t;
    }
}
