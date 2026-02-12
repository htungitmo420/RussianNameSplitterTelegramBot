package vn.nhtw420.russiannamesplitter.application.usecase;

import vn.nhtw420.russiannamesplitter.application.dto.request.SplitFioRequest;
import vn.nhtw420.russiannamesplitter.application.dto.response.SplitFioResponse;
import vn.nhtw420.russiannamesplitter.application.port.FioSplitterPort;
import vn.nhtw420.russiannamesplitter.application.port.ShortNamePort;
import vn.nhtw420.russiannamesplitter.application.port.TextNormalizerPort;
import vn.nhtw420.russiannamesplitter.common.exception.BadInputException;

import java.util.List;
import java.util.Objects;

public class SplitFioUseCase {

    private final TextNormalizerPort normalizer;
    private final FioSplitterPort splitter;
    private final ShortNamePort shortNamePort;

    public SplitFioUseCase(TextNormalizerPort normalizer, FioSplitterPort splitter, ShortNamePort shortNamePort) {
        this.normalizer = Objects.requireNonNull(normalizer);
        this.splitter = Objects.requireNonNull(splitter);
        this.shortNamePort = Objects.requireNonNull(shortNamePort);
    }

    public SplitFioResponse execute(SplitFioRequest request) {
        if (request == null || request.rawInput() == null || request.rawInput().isBlank()) {
            throw new BadInputException("Input is empty");
        }

        String normalized = normalizer.normalizeNameInput(request.rawInput());

        if (normalized.isBlank()) {
            throw new BadInputException("Input is empty after normalization");
        }

        FioSplitterPort.SplitResult r = splitter.split(normalized);

        // If given name is detected, suggest short name
        List<String> shortNames = (r.givenName() == null || r.givenName().isBlank())
                ? List.of()
                : safeList(shortNamePort.suggestShortNames(r.givenName()));

        return new SplitFioResponse(
                blankToNull(r.surname()),
                blankToNull(r.givenName()),
                blankToNull(r.patronymic()),
                shortNames,
                r.confidence()
        );
    }

    private static List<String> safeList(List<String> xs) {
        return xs == null ? List.of() :List.copyOf(xs);
    }

    private static String blankToNull(String s) {
        if (s == null) return null;

        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
