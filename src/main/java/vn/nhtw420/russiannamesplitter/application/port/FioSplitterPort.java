package vn.nhtw420.russiannamesplitter.application.port;

public interface FioSplitterPort {
    SplitResult split(String normalizedInput);

    record SplitResult(
            String surname,
            String givenName,
            String patronymic,
            double confidence
    ){}
}
