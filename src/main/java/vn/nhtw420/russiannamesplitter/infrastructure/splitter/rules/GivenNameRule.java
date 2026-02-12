package vn.nhtw420.russiannamesplitter.infrastructure.splitter.rules;

import vn.nhtw420.russiannamesplitter.infrastructure.splitter.support.NameHeuristics;
import vn.nhtw420.russiannamesplitter.infrastructure.splitter.support.SplitContext;

import java.util.List;

public class GivenNameRule implements Rule{
    @Override
    public void apply(SplitContext context) {
        List<String> strings = context.tokens();

        if (strings.isEmpty()) return;

        if (strings.size() == 1) {
            return;
        }

        if (strings.size() == 2) {
            String first = strings.get(0);
            String second = strings.get(1);

            // If patronymic detected as second token:
            // Decide whether first is GivenName or Surname
            if (context.patronymic() != null && context.patronymic().equals(second)) {
                if (NameHeuristics.looksLikeSurname(first)) {
                    // "Surname Patronymic" -> leave givenName empty
                    return;
                } else {
                    // "GivenName Patronymic"
                    context.setGivenName(first);
                    context.addConfidence(0.10);
                    return;
                }
            }

            // default common: "Surname GivenName"
            context.setGivenName(second);
            context.addConfidence(0.15);
            return;
        }

        // 3+ tokens
        if (context.patronymic() != null) {
            context.setGivenName(strings.get(strings.size() - 2));
            context.addConfidence(0.15);
            return;
        }

        context.setGivenName(strings.get(1));
        context.addConfidence(0.10);
    }
}
