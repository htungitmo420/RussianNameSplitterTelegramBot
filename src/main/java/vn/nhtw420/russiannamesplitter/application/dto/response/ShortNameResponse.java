package vn.nhtw420.russiannamesplitter.application.dto.response;

import java.util.List;

public record ShortNameResponse(
        String givenName,
        List<String> shortNames
) {}
