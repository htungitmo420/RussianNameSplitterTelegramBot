package vn.nhtw420.russiannamesplitter.infrastructure.splitter.rules;

import vn.nhtw420.russiannamesplitter.infrastructure.splitter.support.NameHeuristics;
import vn.nhtw420.russiannamesplitter.infrastructure.splitter.support.SplitContext;

import java.util.List;

public class SurnameRule implements Rule{

    @Override
    public void apply(SplitContext context) {
        List<String> strings = context.tokens();

        if (strings.isEmpty()) return;

        if (strings.size() == 1) {
            return;
        }

        if (strings.size() == 2) {
            String first = strings.getFirst();
            String second = strings.get(1);

            // If second is patronymic, decide if first is surname
            if (context.patronymic() != null && context.patronymic().equals(second)) {
                if (NameHeuristics.looksLikeSurname(first)) {
                    context.setSurname(first);
                    context.addConfidence(0.20);
                }
                return;
            }

            // default: "Surname Given"
            context.setSurname(first);
            context.addConfidence(0.20);
            return;
        }

        // 3+ tokens
        if (context.patronymic() != null && context.givenName() != null) {
            int givenIdx = strings.size() - 2;
            if (givenIdx >= 1) {
                context.setSurname(String.join(" ", strings.subList(0, givenIdx)));
                context.addConfidence(0.20);
                return;
            }
        }

        context.setSurname(strings.get(0));
        context.addConfidence(0.15);
    }
}
