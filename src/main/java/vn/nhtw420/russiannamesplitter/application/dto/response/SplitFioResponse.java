package vn.nhtw420.russiannamesplitter.application.dto.response;

import java.util.List;

public record SplitFioResponse(
        String surname,
        String givenName,
        String patronymic,
        List<String> shortNames,
        double confidence
) {}
