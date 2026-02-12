package vn.nhtw420.russiannamesplitter.application.usecase;

import vn.nhtw420.russiannamesplitter.application.dto.request.ShortNameRequest;
import vn.nhtw420.russiannamesplitter.application.dto.response.ShortNameResponse;
import vn.nhtw420.russiannamesplitter.application.port.ShortNamePort;
import vn.nhtw420.russiannamesplitter.common.exception.BadInputException;

import java.util.List;

public class SuggestShortNameUseCase {

    private final ShortNamePort shortNamePort;

    public SuggestShortNameUseCase(ShortNamePort shortNamePort) {
        this.shortNamePort = shortNamePort;
    }

    public ShortNameResponse execute(ShortNameRequest request) {
        if (request == null || request.givenName() == null || request.givenName().isBlank()) {
            throw new BadInputException("Given name is empty");
        }

        String givenName = request.givenName().trim();
        List<String> shortNames = shortNamePort.suggestShortNames(givenName);

        return new ShortNameResponse(givenName, shortNames == null ? List.of() : List.copyOf(shortNames));
    }
}
