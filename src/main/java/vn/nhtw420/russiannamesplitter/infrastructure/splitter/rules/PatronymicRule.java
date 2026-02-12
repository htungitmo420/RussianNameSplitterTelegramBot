package vn.nhtw420.russiannamesplitter.infrastructure.splitter.rules;

import vn.nhtw420.russiannamesplitter.infrastructure.splitter.support.NameHeuristics;
import vn.nhtw420.russiannamesplitter.infrastructure.splitter.support.SplitContext;

import java.util.List;
import java.util.Locale;

public class PatronymicRule implements Rule {

    @Override
    public void apply(SplitContext context) {
        List<String> tokens = context.tokens();
        if (tokens == null || tokens.isEmpty()) return;

        if (tokens.size() == 1) {
            String candidate = tokens.getFirst();
            if (NameHeuristics.looksLikePatronymic(candidate)) {
                context.setPatronymic(candidate);
                context.addConfidence(0.35);
            }
            return;
        }

        if (tokens.size() == 2) {
            String candidate = tokens.get(1);
            if (NameHeuristics.looksLikePatronymic(candidate)) {
                context.setPatronymic(candidate);
                context.addConfidence(0.15);
            }
            return;
        }

        String last = tokens.getLast();
        if (NameHeuristics.looksLikePatronymic(last)) {
            context.setPatronymic(last);
            context.addConfidence(0.25);
            return;
        }

        // Optional fallback:
        // Some people might write "Given Patronymic Surname" -> patronymic is 2nd token
        // only apply if it looks very much like patronymic
        String second = tokens.get(1);
        if (NameHeuristics.looksLikePatronymic(second)) {
            context.setPatronymic(second);
            context.addConfidence(0.18);
        }
    }

    private static String normalize(String s) {
        String t = s.trim().toLowerCase(Locale.ROOT);

        // unify ё -> е for matching
        t = t.replace('ё', 'е');

        return t;
    }
}
