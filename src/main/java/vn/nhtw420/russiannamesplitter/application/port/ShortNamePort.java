package vn.nhtw420.russiannamesplitter.application.port;

import java.util.List;

public interface ShortNamePort {
    List<String> suggestShortNames(String givenName);
}
